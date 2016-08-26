package com.zebra.xconfig.server;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zebra.xconfig.common.Constants;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.imps.DefaultACLProvider;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ying on 16/8/8.
 */

public class CuratorTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    CuratorFramework client = null;
    Boolean isConnected = false;
    List<ACL> acls;

    @Before
    public void init() throws Exception{
        acls = new ArrayList<>();

        Id id = new Id("digest", DigestAuthenticationProvider.generateDigest("xconfig:xconfig"));
        ACL acl = new ACL(ZooDefs.Perms.ALL,id);
        acls.add(acl);

//        AuthInfo authInfo = new AuthInfo("digest",DigestAuthenticationProvider.generateDigest("xconfig:xconfig").getBytes("UTF-8"));
//        logger.debug("===>{}",authInfo.toString());

        RetryNTimes retry = new RetryNTimes(1, 2);
        //创建zk客户端
        client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(retry)
                .connectionTimeoutMs(1000 * 16)
                .sessionTimeoutMs(1000 * 60)
                .aclProvider(new MyAclProvider())
                .authorization("digest", "xconfig:xconfig".getBytes())
                .namespace(Constants.NAME_SPACE)
                .build();

        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                isConnected = connectionState.isConnected();
                logger.debug("connectionState is {},isConnected:{}",connectionState,connectionState.isConnected());
            }
        });

        client.start();
        client.blockUntilConnected(10, TimeUnit.SECONDS);
    }

    @Test
    public void testSleep() throws Exception{
        Thread.sleep(1000*60*10);
    }

    @Test
    public void test() throws Exception{
        logger.debug("-----------test begin-----------------");
        client.checkExists().forPath("/mysql");
        client.checkExists().forPath("/mysql");
        client.checkExists().forPath("/mysql");
        logger.debug("-----------test end-----------------");
    }

    @Test
    public void testPathChildCache() throws Exception{
//        final NodeCache nodeCache = new NodeCache(client,"/test/dev");
//        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//
//            }
//        };

        final PathChildrenCache pathChildrenCache = new PathChildrenCache(client,"/test/dev",true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                logger.info("event:{}",event.getType());
            }
        };

        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        client.checkExists().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                logger.info("event:{}", event.getType());

                try {
                    if (Event.EventType.NodeDeleted == event.getType()) {
                    } else if (Event.EventType.NodeCreated == event.getType()) {
                        pathChildrenCache.clearAndRefresh();
                    } else {

                    }


                    client.checkExists().usingWatcher(this).forPath("/test/dev");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).forPath("/test/dev");

        System.in.read();

    }

    @Test
    public void testAcl(){
        try{
            client.create().creatingParentsIfNeeded().forPath("/ttt/test", "0".getBytes());
            Stat stat = client.checkExists().forPath("/_leaderSelector");

//            LeaderLatch leaderLatch = new LeaderLatch(client, Constants.LEADER_SELECT_PATH, UUID.randomUUID().toString());
//            leaderLatch.start();
//            leaderLatch.addListener(new LeaderLatchListener() {
//                @Override
//                public void isLeader() {
//                    logger.debug("===>isLeader!");
//                }
//
//                @Override
//                public void notLeader() {
//                    logger.debug("====>notLeader");
//                }
//            });

            logger.debug("=====>{}", JSON.toJSONString(stat));
            Thread.sleep(1000*60*30);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

    }

    class MyAclProvider extends DefaultACLProvider{
        @Override
        public List<ACL> getDefaultAcl() {
            return super.getDefaultAcl();
        }

        @Override
        public List<ACL> getAclForPath(String path) {
            return acls;
        }
    }
}
