package com.zebra.xconfig.server.util.zk;

import com.zebra.xconfig.common.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by ying on 16/7/18.
 */
public class XConfigCuratorFactory {

    public static CuratorFramework init(String zkConnStr){
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000 * 1, 5);
        //创建zk客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkConnStr)
                .retryPolicy(retry)
                .connectionTimeoutMs(1000 * 16)
                .sessionTimeoutMs(1000 * 60)
                .namespace(Constants.NAME_SPACE)
                .build();
        client.start();

        return client;
    }
}
