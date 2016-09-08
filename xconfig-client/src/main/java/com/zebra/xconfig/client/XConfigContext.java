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
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    private CountDownLatch countDownLatch;//zk初始化闭锁
    private volatile  boolean initOk = false;//是否初始化成功

    private XKeyObservable xKeyObservable;

    //生成备份文件的时候多写入的两个备份字段
    private final String _MY_CREATE_TIME = "createTime";
    private final String _MY_PROFILE = "profile";

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
                XZkClient zkClient = XZkClient.getxZkClient();
                if(zkClient.isConnected()) {
                    logger.info("zk已连接，使用zk启动");
                    final CuratorFramework client = zkClient.getClient();

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
                        final String profilePath = CommonUtil.genProfilePath(tmp, XConfigFactory.getProfile());

                        if ("0".equals(tmp) || StringUtils.isBlank(tmp)) {
                            countDownLatch.countDown();
                            continue;
                        }

                        //写_client节点
                        StringBuilder nodeName = new StringBuilder();
                        nodeName.append(tmp)
                                .append("|")
                                .append(XConfigFactory.getProfile())
                                .append(":");
                        try {
                            InetAddress inetAddress = InetAddress.getLocalHost();
                            nodeName.append(inetAddress.getHostAddress());
                        } catch (UnknownHostException e) {
                            nodeName.append("unknownHost");
                        }
                        nodeName.append(":").append(UUID.randomUUID().toString());
                        try {
                            client.create()
                                    .creatingParentsIfNeeded()
                                    .withMode(CreateMode.EPHEMERAL)
                                    .forPath(Constants.CLIENT_REGIST_PATH +"/" + nodeName, "0".getBytes());
                        } catch (Exception e) {
                            throw new XConfigException(e.getMessage(),e);
                        }

                        logger.debug("===>监听子节点{}", profilePath);
                        //监听key
                        final PathChildrenCache keyCache = new PathChildrenCache(client, profilePath, true);
                        KeyCacheListener keyCacheListener = new KeyCacheListener(profilePath);
                        keyCache.getListenable().addListener(keyCacheListener);
                        keyCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

                        //监听profile路径，解决profile删除之后重建会导致监听失效的问题！(PathChildrenCache 存在这种问题）
                        client.checkExists().usingWatcher(new Watcher() {
                            @Override
                            public void process(WatchedEvent event) {
                                try {
                                    if (Event.EventType.NodeDeleted == event.getType()) {
                                    } else if (Event.EventType.NodeCreated == event.getType()) {
                                        keyCache.clearAndRefresh();
                                    } else {

                                    }

                                    client.checkExists().usingWatcher(this).forPath(profilePath);
                                } catch (Exception e) {
                                    logger.error(e.getMessage(),e);
                                }
                            }
                        }).forPath(profilePath);

                        logger.debug("===>子节点监听设置完成");
                    }
                    initOk = countDownLatch.await(60, TimeUnit.SECONDS);//等待初始化完成
                }else{
                    //使用zk失败，做降级处理，使用上次启动时候的文件启动应用
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
                        if( !_MY_CREATE_TIME.equals(key) && !_MY_PROFILE.equals(key)){
                            logger.debug("==========>key:{}",key);
                            this.cacheDepProject.put(CommonUtil.genProjectByMkey(key), "");
                        }
                    }

                    initOk = true;
                }

                this.writeFile(Constants.BOOT_FILE);

                //写备份文件
                this.writeBootHistoryFile();
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

    /**
     * 写文件到configDir下
     * @param fileName
     */
    private void writeFile(String fileName){
        FileOutputStream fileOutputStream = null;

        try {
            Properties properties = this.getProperties();
            fileOutputStream = new FileOutputStream(this.xConfig.getLocalConfigDir() + File.separator + fileName);
            properties.setProperty("createTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            properties.setProperty("profile",XConfigFactory.getProfile());
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
     * 此方法在每次初始化完成的时候调用，在configDir/bootHis目录下记录每次启动时候的properties文件快照
     */
    private void writeBootHistoryFile(){
        FileOutputStream fileOutputStream = null;

        try {
            String bootHisDir = this.xConfig.getLocalConfigDir() + File.separator + Constants.LOCAL_BOOT_HIS_DIR;

            Date now = new Date();
            String currentTimeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(now);

            Properties properties = this.getProperties();
            fileOutputStream = new FileOutputStream(bootHisDir + File.separator + Constants.BOOT_FILE + "." +currentTimeStr);
            properties.setProperty("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
            properties.setProperty("profile",XConfigFactory.getProfile());
            properties.store(fileOutputStream,"generate by xConfig");


            //最多保留Constants.BOOT_HIS_MAX_COUNT条快照记录
            File bootHisFileDir = new File(bootHisDir);
            String[] bootHisFiles = bootHisFileDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(name.startsWith(Constants.BOOT_FILE)){
                        return true;
                    }else {
                        return false;
                    }
                }
            });
            List<Long> fileSuffixs = new ArrayList<>();
            for (String bootHisFile : bootHisFiles) {
                String[] datas = bootHisFile.split("\\.");
                if (datas.length != 3) {
                    continue;
                }
                fileSuffixs.add(Long.valueOf(datas[2]));
            }
            Collections.sort(fileSuffixs, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    return o1 - o2 > 0 ? -1 : 1;
                }
            });
            if(fileSuffixs.size() > Constants.BOOT_HIS_MAX_COUNT){
                for(int i = Constants.BOOT_HIS_MAX_COUNT ; i < fileSuffixs.size() ; i++){
                    File file = new File(bootHisDir + File.separator + Constants.BOOT_FILE + "." +fileSuffixs.get(i));
                    file.delete();
                }
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

    protected XKeyObservable getxKeyObservable() {
        return xKeyObservable;
    }

    protected Map<String, String> getCacheDepProject() {
        return cacheDepProject;
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
