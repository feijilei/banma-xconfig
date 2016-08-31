package com.zebra.xconfig.client;

import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.common.exception.XConfigException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ying on 16/8/31.
 */
public class XConfigFactory {
    private static Logger logger = LoggerFactory.getLogger(XConfigFactory.class);

    private static Map<String,XConfig> xconfigs = new HashMap<>();

    private static String xconfigDir;//xconfig的默认目录
    /**
     * 是否只支持一个project，默认我们只允许一个project，推荐这种用法，当此处为true的时候，只允许加载一个project（实例化多个Xconfig），否则会报错
     * 可以通过设置jvm参数 -Dxconfig.isSingleProject=false 支持多个project，可以实例化多个xonfig
     */
    private static Boolean isSingleProject = true;//是否只支持单个project
    private static String zkConn;
    private static String profile;
    private static String userName;
    private static String password;

    static{
        try {
            xconfigDir = System.getProperty("user.home")
                    + File.separator + Constants.LOCAL_FILE_DIR_NAME;

            String singleProject = System.getProperty("xconfig.isSingleProject");
            if (StringUtils.isNotBlank(singleProject)) {
                isSingleProject = Boolean.valueOf(singleProject);
            }

            //获取配置，优先jvm，其次配置文件
            zkConn = System.getProperty("xconfig.zkConn");
            profile = System.getProperty("xconfig.profile");
            userName = System.getProperty("xconfig.userName");
            password = System.getProperty("xconfig.password");
            //配置文件
            File cfFile = new File(xconfigDir + File.separator + Constants.CONFIG_FILE);
            Properties xconfigProp = new Properties();
            try {
                xconfigProp.load(new FileInputStream(cfFile));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new XConfigException("无法获取到配置文件，请确认是否存在：" + cfFile);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new XConfigException("读取配置文件失败：" + cfFile);
            }
            if (StringUtils.isBlank(profile)) {
                profile = xconfigProp.getProperty("profile");
                if (StringUtils.isBlank(profile)) {
                    throw new XConfigException("无法获取到profile信息！");
                }
            }
            if (StringUtils.isBlank(zkConn)) {
                zkConn = xconfigProp.getProperty("zkConn");
            }
            if (StringUtils.isBlank(userName) && StringUtils.isBlank(password)) {
                userName = xconfigProp.getProperty("userName");
                password = xconfigProp.getProperty("password");
            }

            //初始化zk zk只初始化一次
            XZkClient.init(zkConn, userName, password);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 初始化
     * 重复调用的时候，如果project一样，总是返回相同的实例
     * @param project
     * @throws com.zebra.xconfig.common.exception.XConfigException
     */
    public static XConfig instance(String project) throws XConfigException {
        return instance(project,null);
    }

    /**
     * 初始化
     * 重复调用的时候，如果project一样，总是返回相同的实例
     * @param project
     * @param xConfigInitListener
     * @throws com.zebra.xconfig.common.exception.XConfigException
     */
    public static XConfig instance(String project,XConfigInitListener xConfigInitListener) throws XConfigException{
        XConfig xConfig = xconfigs.get(project);
        if(xConfig == null){
            synchronized (XConfigFactory.class) {
                if(isSingleProject && xconfigs.size() > 0){
                    throw new XConfigException("不允许实例化多个xConfig对象");
                }
                xConfig = xconfigs.get(project);
                if(xConfig == null){
                    xConfig = new XConfig(project,xConfigInitListener);
                    xconfigs.put(project, xConfig);
                }
            }
        }

        return xConfig;
    }

    public static String getXconfigDir() {
        return xconfigDir;
    }

    public static Boolean getIsSingleProject() {
        return isSingleProject;
    }

    public static String getZkConn() {
        return zkConn;
    }

    public static String getProfile() {
        return profile;
    }
}

