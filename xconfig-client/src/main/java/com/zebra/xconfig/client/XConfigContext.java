package com.zebra.xconfig.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dianping.cat.Cat;
import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.common.MyAclProvider;
import com.zebra.xconfig.common.exception.XConfigBootException;
import com.zebra.xconfig.common.exception.XConfigException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ying on 16/7/15.
 *
 * 注册TreeEvent当节点不存在的时候，性能开销比较大，不使用这种监听器
 * //todo 依赖信息是否要动态监听 项目配置好的情况下修改频繁修改依赖的情况并不多见，这种情况大部分都需要重启应用，所以不需要动态监听
 */
public class XConfigContext {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //存储配置信息
    private final Map<String,String> cacheKv = new ConcurrentHashMap<String, String>(300);
    //项目依赖信息
    private final Map<String,String> cacheDepProject = new ConcurrentHashMap<String, String>(50);

    private XConfig xConfig;//配置信息

    private CuratorFramework client;//zk客户端
    private CountDownLatch countDownLatch;//zk初始化闭锁
    private volatile  boolean initOk = false;//是否初始化成功
    private volatile boolean zkConnected = false;//zk是否在线

    private XKeyObservable xKeyObservable;

    XConfigContext(final XConfig xConfig,XKeyObservable xKeyObservable) throws XConfigException{
        this.xKeyObservable = xKeyObservable;
        this.xConfig = xConfig;

        String localFilePath = this.xConfig.getLocalConfigDir() + File.separator + Constants.DEFAULT_FILE;

        FileInputStream fileInputStream = null;
        try {

            File localFile = new File(localFilePath);

            //local.properties存在，启动本地模式
            if(localFile.exists() && localFile.isFile()){
                logger.info("检测到{}文件，启动本地模式",localFile);
                Cat.logError(new XConfigBootException("xconfig启动本地模式："+localFilePath));

                fileInputStream = new FileInputStream(localFile);
                Properties properties = new Properties();
                properties.load(fileInputStream);

                for(String key : properties.stringPropertyNames()){
                    this.cacheKv.put(key,properties.getProperty(key));
                }

                initOk = true;
            }else {//zk启动
                ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
                //创建zk客户端
                CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                        .connectString(xConfig.getZkConn())
                        .retryPolicy(retry)
                        .connectionTimeoutMs(1000 * 16)
                        .sessionTimeoutMs(1000 * 60)
                        .namespace(Constants.NAME_SPACE);
                if (StringUtils.isNotBlank(xConfig.getUserName()) && StringUtils.isNotBlank(xConfig.getPassword())){
                    builder.aclProvider(new MyAclProvider(xConfig.getUserName(),xConfig.getPassword()));
                    builder.authorization("digest",(xConfig.getUserName()+":"+xConfig.getPassword()).getBytes());
                }
                client = builder.build();
                client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                    @Override
                    public void stateChanged(CuratorFramework client, ConnectionState newState) {
                        logger.info("zk stateChanged state:{},connected:{}",newState,newState.isConnected());
                        zkConnected = newState.isConnected();
                    }
                });
                client.start();
                zkConnected = client.blockUntilConnected(10,TimeUnit.SECONDS);

                if(zkConnected) {
                    logger.info("zk已连接，使用zk启动");
                    //当前项目依赖
                    //        final NodeCache projectNode = new NodeCache(client,"/"+xConfig.getProject());
                    //        NodeCacheListener projectNodeListener = new NodeCacheListener() {
                    //            @Override
                    //            public void nodeChanged() throws Exception {
                    //                logger.debug("projectNodeChange:{}",projectNode.getCurrentData().getPath());
                    //
                    //                for()
                    //            }
                    //        };

                    String depStr = new String(client.getData().forPath("/" + xConfig.getProject()));

                    List<String> dependencies = new ArrayList<String>();
                    dependencies.addAll(Arrays.asList(depStr.split(",")));
                    dependencies.add(xConfig.getProject());
                    for (String tmp : dependencies) {
                        this.cacheDepProject.put(tmp, "");
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("===>依赖检查完毕:{}", JSONArray.toJSONString(dependencies));
                    }


                    //注册profile监听子节点
                    this.countDownLatch = new CountDownLatch(dependencies.size());
                    for (String tmp : dependencies) {
                        String profilePath = CommonUtil.genProfilePath(tmp, xConfig.getProfile());

                        if ("0".equals(tmp)) {
                            countDownLatch.countDown();
                            continue;
                        }
                        logger.debug("===>监听子节点{}", profilePath);
                        //监听key
                        final PathChildrenCache keyCache = new PathChildrenCache(client, profilePath, true);
                        KeyCacheListener keyCacheListener = new KeyCacheListener(profilePath);
                        keyCache.getListenable().addListener(keyCacheListener);
                        keyCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

                        logger.debug("===>子节点监听设置完成");
                    }
                    initOk = countDownLatch.await(60, TimeUnit.SECONDS);//等待初始化完成
                }else{
                    //使用zk失败，做降级处理，使用上次启动时候的文件启动应用
                    client.close();
                    logger.warn("zk初始化失败，尝试使用最后一次更新的配置文件启动");
                    File currentFile = new File(xConfig.getLocalConfigDir()+File.separator+Constants.CURRENT_FILE);
                    File bootFile = new File(xConfig.getLocalConfigDir()+File.separator+Constants.BOOT_FILE);
                    File userFile = null;
                    if(currentFile.exists()){
                        logger.warn("使用{}启动",currentFile.getAbsolutePath());
                        Cat.logError(new XConfigBootException("xconfig降级启动:"+currentFile.getAbsolutePath()));

                        userFile = currentFile;
                    }else if(bootFile.exists()){
                        logger.warn("使用{}启动",bootFile.getAbsoluteFile());
                        Cat.logError(new XConfigBootException("xconfig降级启动:"+currentFile.getAbsolutePath()));

                        userFile = bootFile;
                    }else{
                        throw new XConfigException("zk启动失败，尝试使用最近的配置文件启动失败，请检查！");
                    }

                    fileInputStream = new FileInputStream(userFile);
                    Properties properties = new Properties();
                    properties.load(fileInputStream);

                    for(String key : properties.stringPropertyNames()){
                        this.cacheKv.put(key,properties.getProperty(key));
                    }

                    initOk = true;
                }

                this.writeFile(Constants.BOOT_FILE);
            }
        } catch (Exception e) {
            initOk = false;
            logger.error(e.getMessage(),e);
        }finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }

        if(initOk){
            this.xKeyObservable.ready();
        }else{
            throw new XConfigException("XConfigContext init failed");
        }
        logger.info("===>初始化完毕,key:{},dependencyProject:{}", JSON.toJSONString(this.cacheKv), JSON.toJSONString(this.cacheDepProject.keySet()));

    }

    public String getValue(String key){
        return cacheKv.get(key);
    }

    public Properties getProperties(){
        Properties properties = new Properties();
        for(String key : this.cacheKv.keySet()){
            properties.setProperty(key,this.cacheKv.get(key));
        }

        return properties;
    }

    private void writeFile(String fileName){
        FileOutputStream fileOutputStream = null;

        try {
            Properties properties = this.getProperties();
            fileOutputStream = new FileOutputStream(this.xConfig.getLocalConfigDir() + File.separator + fileName);
            properties.setProperty("createTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            properties.setProperty("profile",xConfig.getProfile());
            properties.store(fileOutputStream,"generate by xConfig");

            if(logger.isDebugEnabled()){
                properties.list(System.out);
            }

        }catch (IOException e){
            logger.error(e.getMessage(),e);
        }finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
    }

    /**
     * Key监听器
     */
    class KeyCacheListener implements PathChildrenCacheListener{
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
                    countDownLatch.countDown();
                    break;
                }

                case CHILD_ADDED: {

                }

                case CHILD_UPDATED: {
                    String path = event.getData().getPath();
                    String data = new String(event.getData().getData());
                    logger.debug("===>keyListener {},data:{}", path, data);
                    String key = CommonUtil.genKey(path);
                    String value = new String(data);
                    cacheKv.put(key, value);
                    xKeyObservable.change(key,value);
                    break;
                }

                case CHILD_REMOVED: {
                    String path = event.getData().getPath();
                    logger.debug("===>keyListener {}", path);
                    String key = CommonUtil.genKey(path);
                    cacheKv.remove(key);
                    xKeyObservable.change(key,null);
                    break;
                }
            }

            //每次变动都写入文件
            if(initOk && (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED
                    || event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED
                    || event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                writeFile(Constants.CURRENT_FILE);
            }
        }
    }
}
