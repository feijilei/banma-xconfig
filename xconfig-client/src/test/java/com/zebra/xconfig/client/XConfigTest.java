package com.zebra.xconfig.client;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ying on 16/8/11.
 */
public class XConfigTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 测试同一个profile注册多个watch时候的性能问题
     */
    @Test
    public void testWatchCount(){
        for(int i = 0 ; i < 1000 ; i++){
            CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181",new RetryNTimes(3,10));

            PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,"/mysql/daily",true);
            pathChildrenCache.getListenable().addListener(new KeyCacheListener("/mysql/daily"));
        }

        try {
            Thread.sleep(1000*60*30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Key监听器
     */
    class KeyCacheListener implements PathChildrenCacheListener {
        private String path;

        public KeyCacheListener(String path){
            this.path = path;
        }
        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            logger.debug("===>keyListener {} event:{}",this.path, event.getType());

            switch (event.getType()) {
                case INITIALIZED: {
                    if(logger.isDebugEnabled()) {
                        logger.debug("===>keyListener {} initialized",this.path);
                    }
                    break;
                }

                case CHILD_ADDED: {

                }

                case CHILD_UPDATED: {
                    String path = event.getData().getPath();
                    String data = new String(event.getData().getData());
                    logger.debug("===>keyListener {},data:{}", path, data);
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
