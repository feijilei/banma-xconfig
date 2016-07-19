package com.zebra.xconfig.server.util.zk;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.server.dao.mapper.XKvMapper;
import com.zebra.xconfig.server.po.KvPo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by ying on 16/7/18.
 */
public class XConfigServer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CuratorFramework client;
    @Autowired
    private XKvMapper xKvMapper;

    private boolean isLeader;

    public void init(){
        LeaderLatch leaderLatch = new LeaderLatch(client, Constants.LEADER_SELECT_PATH, UUID.randomUUID().toString());
        this.isLeader = leaderLatch.hasLeadership();

        Timer timer = new Timer(true);
        TimerTask synTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    logger.debug("synTask running,isLeader:{}",isLeader);

                    Stat stat = client.checkExists().forPath("/");
                    if(isLeader && System.currentTimeMillis() - stat.getMtime() > Constants.SYN_PERIOD_MILLIS){//超过10分钟没有同步过
                        synDb2Zk();
                        client.setData().forPath("/", "0".getBytes());
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            }
        };
        timer.schedule(synTask,0,Constants.SYN_PERIOD_MILLIS);
    }

    /**
     * 同步数据库中所有节点到zk
     */
    private void synDb2Zk(){
        logger.debug("开始同步数据");
        List<KvPo> kvPos = this.xKvMapper.queryAll();
        for(KvPo kvPo : kvPos){
            String keyPath = CommonUtil.genKeyPath(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey());

            try {
                byte[] data = client.getData().forPath(keyPath);
                if(data == null || !kvPo.getxValue().equals(new String(data).toString())){
                    client.setData().forPath(keyPath,kvPo.getxValue().getBytes());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
        logger.debug("同步数据结束");
    }

    public boolean isLeader() {
        return isLeader;
    }
}
