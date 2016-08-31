package com.zebra.xconfig.client;

import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.common.MyAclProvider;
import com.zebra.xconfig.common.exception.XConfigException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by ying on 16/8/25.
 */
public class XZkClient {
    private static final Logger logger = LoggerFactory.getLogger(XZkClient.class);

    private static XZkClient xZkClient = null;

    private String zkConn;
    private String userName;
    private String password;

    private CuratorFramework client;
    private boolean zkConnected = false;

    private XZkClient(String zkConn,String userName,String password) throws XConfigException{
        this.zkConn = zkConn;
        this.userName = userName;
        this.password = password;

        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(2000, 8);
        //创建zk客户端
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConn)
                .retryPolicy(retry)
                .connectionTimeoutMs(1000 * 16)
                .sessionTimeoutMs(1000 * 30)
                .namespace(Constants.NAME_SPACE);
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            builder.aclProvider(new MyAclProvider(userName, password));
            builder.authorization("digest", (userName + ":" + password).getBytes());
        }
        client = builder.build();
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.info("zk stateChanged state:{},connected:{}", newState, newState.isConnected());
                zkConnected = newState.isConnected();
            }
        });
        client.start();
        try {
            zkConnected = client.blockUntilConnected(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new XConfigException(e.getMessage(),e);
        }

        //注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                logger.debug("shutdown hook");
                if(client.getState() == CuratorFrameworkState.STARTED){
                    logger.debug("zkClient close");
                    client.close();
                }
            }
        });
    }

    public static XZkClient init(String zkConn,String userName,String password) throws XConfigException{
        if(xZkClient == null) {
            synchronized (XZkClient.class) {
                if (xZkClient == null) {
                    xZkClient = new XZkClient(zkConn, userName, password);
                } else {
                    logger.warn("XZkClient只能被初始化一次！");
                }
            }
        }else{
            logger.warn("XZkClient只能被初始化一次！");
        }

        return xZkClient;
    }

    public static XZkClient getxZkClient() throws XConfigException{
        if(xZkClient == null){
            throw new XConfigException("XZkClient尚未初始化！");
        }else{
            return xZkClient;
        }
    }

    public CuratorFramework getClient(){
        return client;
    }

    public boolean isConnected(){
        return zkConnected;
    }

    public String getZkConn() {
        return zkConn;
    }
}
