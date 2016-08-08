package com.zebra.xconfig.server.util.zk;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.dao.mapper.XKvMapper;
import com.zebra.xconfig.server.dao.mapper.XProjectProfileMapper;
import com.zebra.xconfig.server.po.KvPo;
import com.zebra.xconfig.server.po.ZkNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ying on 16/7/18.
 */
public class XConfigServer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CuratorFramework client;
    @Autowired
    private XKvMapper xKvMapper;
    @Autowired
    private XProjectProfileMapper xProjectProfileMapper;

    private volatile boolean isLeader = false;
    private volatile boolean timerRunning = false;
    private volatile boolean zkConnected = false;

    public void init() throws Exception{
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.info("zk stateChanged state:{},connected:{}",newState,newState.isConnected());
                zkConnected = newState.isConnected();
            }
        });
        zkConnected = client.blockUntilConnected(30, TimeUnit.SECONDS);
        if(!zkConnected){
            throw new XConfigException("zk连接超时失败！");
        }

        LeaderLatch leaderLatch = new LeaderLatch(client, Constants.LEADER_SELECT_PATH, UUID.randomUUID().toString());
        leaderLatch.start();

        final Timer timer = new Timer(true);
        final TimerTask synTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    logger.debug("synTask running,isLeader:{}",isLeader);

                    if(isLeader && zkConnected) {
                        Stat stat = client.checkExists().forPath("/");
                        if (System.currentTimeMillis() - stat.getMtime() > Constants.SYN_PERIOD_MILLIS) {//超过10分钟没有同步过
                            synDb2Zk();
                            client.setData().forPath("/", "0".getBytes());
                        }
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            }
        };

        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                isLeader = true;

                synchronized (this) {
                    if (!timerRunning) {
                        timer.schedule(synTask, 0, Constants.SYN_PERIOD_MILLIS);
                        timerRunning = true;
                    }
                }
            }

            @Override
            public void notLeader() {
                isLeader = false;
            }
        });

        logger.debug("xConfigServer init end");

    }

    /**
     * 同步数据库中所有节点到zk
     * 同步依赖信息
     */
    private void synDb2Zk(){
        logger.debug("开始同步kv数据");
        List<KvPo> kvPos = this.xKvMapper.queryAll();
        for(KvPo kvPo : kvPos){
            String keyPath = CommonUtil.genMKeyPath(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey());

            try {
                Stat stat = client.checkExists().forPath(keyPath);

                if(stat == null){
                    client.create().creatingParentsIfNeeded().forPath(keyPath,kvPo.getxValue().getBytes());
                    logger.debug("创建节点{}",keyPath);
                }else{
                    String value = new String(client.getData().forPath(keyPath));
                    if(!value.equals(kvPo.getxValue())) {
                        client.setData().forPath(keyPath, kvPo.getxValue().getBytes());
                        logger.debug("更新节点", keyPath);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
        logger.debug("同步kv结束");

        logger.debug("开始同步依赖信息");
        List<String> projects = this.xProjectProfileMapper.queryAllProjects();
        for (String project : projects){
            try{
                String projectPath = CommonUtil.genProjectPath(project);

                List<String> dependencies = this.xProjectProfileMapper.queryProjectDependencies(project);

                String depStr = StringUtils.join(dependencies,",");

                String zkStr = new String(client.getData().forPath(projectPath));

                if(!depStr.equals(zkStr)){
                    client.setData().forPath(projectPath,depStr.getBytes());
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        logger.debug("依赖信息同步结束");

        //todo 是否需要增加个反向验证，如果zk存在脏的节点，而mysql中并没有怎么办，我们目前只是保证mysql中有的zk上也有
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void createUpdateKvNode(String nodePath,String value) throws Exception{
        if(!zkConnected){
            throw new XConfigException("zk is not Connected");
        }
        Stat stat = client.checkExists().forPath(nodePath);
        if(stat == null){
            client.create().creatingParentsIfNeeded().forPath(nodePath,value.getBytes());
        }else{
            client.setData().forPath(nodePath,value.getBytes());
        }
    }

    public void createKvNodesWithTransaction(List<ZkNode> zkNodes) throws Exception{
        if(!zkConnected){
            throw new XConfigException("zk is not Connected");
        }
        if(zkNodes == null || zkNodes.size() == 0){
            return;
        }
        CuratorTransaction curatorTransaction = client.inTransaction();
        CuratorTransactionFinal curatorTransactionFinal = null;
        for(ZkNode zkNode : zkNodes){
            curatorTransactionFinal = this.addCreateToTransaction(curatorTransactionFinal == null ? curatorTransaction : curatorTransactionFinal,zkNode);
        }

        if(curatorTransactionFinal != null){
            curatorTransactionFinal.commit();
        }
    }

    /**
     * 此方法将删除节点和所有子节点
     * @param nodePath
     * @throws Exception
     */
    public void deleteNode(String nodePath) throws  Exception{
        if(!zkConnected){
            throw new XConfigException("zk is not Connected");
        }
        Stat stat = client.checkExists().forPath(nodePath);
        if(stat != null){
            client.delete().deletingChildrenIfNeeded().forPath(nodePath);
        }
    }

    public void deleteKvNodesWithTransaction(List<ZkNode> zkNodes) throws Exception{
        if(!zkConnected){
            throw new XConfigException("zk is not Connected");
        }
        if(zkNodes == null || zkNodes.size() == 0){
            return;
        }

        CuratorTransaction curatorTransaction = client.inTransaction();
        CuratorTransactionFinal curatorTransactionFinal = null;
        for(ZkNode zkNode : zkNodes){
            curatorTransactionFinal = this.addDeleteToTransaction(curatorTransactionFinal == null ? curatorTransaction : curatorTransactionFinal,zkNode.getPath());
        }

        if(curatorTransactionFinal != null){
            curatorTransactionFinal.commit();
        }
    }

    private CuratorTransactionFinal addCreateToTransaction(CuratorTransaction curatorTransaction,ZkNode zkNode) throws Exception{
        return curatorTransaction.create().forPath(zkNode.getPath(),zkNode.getValue().getBytes()).and();
    }

    private CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction curatorTransaction,String path) throws  Exception{
        return curatorTransaction.delete().forPath(path).and();
    }
    private CuratorTransactionFinal addSetDataToTransaction(CuratorTransaction curatorTransaction,ZkNode zkNode) throws  Exception{
        return curatorTransaction.setData().forPath(zkNode.getPath(), zkNode.getValue().getBytes()).and();
    }
}
