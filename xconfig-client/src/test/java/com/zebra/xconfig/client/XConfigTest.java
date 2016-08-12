package com.zebra.xconfig.client;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.common.MyAclProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ying on 16/8/11.
 */
public class XConfigTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile long startTime = 0;

    private int size = 500;
    private CountDownLatch countDownLatch = new CountDownLatch(size);
    private CountDownLatch countDownLatch2 = new CountDownLatch(size);
    private long[] times = new long[size];
    /**
     * 测试同一个profile注册多个watch时候的性能问题
     */
    @Test
    public void testWatchCount(){
        try {

            //client开启
            for(int i = 0 ; i < size ; i++){
                ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
                //创建zk客户端
                CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                        .connectString("localhost:2181")
                        .retryPolicy(retry)
                        .connectionTimeoutMs(1000 * 16)
                        .sessionTimeoutMs(1000 * 60)
                        .namespace(Constants.NAME_SPACE);

                builder.aclProvider(new MyAclProvider("xconfig","xconfig"));
                builder.authorization("digest", ("xconfig:xconfig").getBytes());

                CuratorFramework curatorFramework = builder.build();

                curatorFramework.start();

                PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,"/mysql/daily",true);
                pathChildrenCache.getListenable().addListener(new KeyCacheListener("/mysql/daily",i));

                pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            }

            //更改数据
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString("localhost:2181")
                    .retryPolicy(new RetryNTimes(1,1000*2))
                    .connectionTimeoutMs(1000 * 16)
                    .sessionTimeoutMs(1000 * 60)
                    .namespace(Constants.NAME_SPACE);
            builder.aclProvider(new MyAclProvider("xconfig","xconfig"));
            builder.authorization("digest", ("xconfig:xconfig").getBytes());
            CuratorFramework client = builder.build();
            client.start();

            countDownLatch.await();

            startTime = System.currentTimeMillis();
            client.setData().forPath("/mysql/daily/jdbc.password","test".getBytes());

            countDownLatch2.await();

            long sum = 0;
            for(long tmp : times){
                sum = sum + tmp;
            }

            logger.info("========>平均耗时：{}",sum/size);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Key监听器
     */
    class KeyCacheListener implements PathChildrenCacheListener {
        private String path;
        private int index;

        public KeyCacheListener(String path,int index){
            this.path = path;
            this.index = index;
        }
        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            logger.debug("===>keyListener {} event:{}",this.path, event.getType());

            switch (event.getType()) {
                case INITIALIZED: {
                    if(logger.isDebugEnabled()) {
                        logger.debug("===>keyListener {} initialized",this.path);
                    }

                    countDownLatch.countDown();
                    break;
                }

                case CHILD_ADDED: {

                }

                case CHILD_UPDATED: {
                    String path = event.getData().getPath();
                    String data = new String(event.getData().getData());
                    logger.debug("===>keyListener {},data:{}", path, data);
                    long t = System.currentTimeMillis() - startTime;
                    times[index] = t;
                    if(event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                        countDownLatch2.countDown();
                    }
                    break;
                }

                case CHILD_REMOVED: {
                    String path = event.getData().getPath();
                    logger.debug("===>keyListener {}", path);
                    String key = CommonUtil.genKey(path);
                    break;
                }
            }
        }
    }
}
