package com.zebra.xconfig.server;

import com.zebra.xconfig.common.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by ying on 16/8/8.
 */

public class CuratorTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    CuratorFramework client = null;
    Boolean isConnected = false;

    @Before
    public void init() throws Exception{
        RetryNTimes retry = new RetryNTimes(1, 2);
        //创建zk客户端
        client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(retry)
                .connectionTimeoutMs(1000 * 16)
                .sessionTimeoutMs(1000 * 60)
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
}
