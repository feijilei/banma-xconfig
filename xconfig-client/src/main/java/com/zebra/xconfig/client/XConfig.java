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
import java.util.Properties;

/**
 * Created by ying on 16/7/15.
 */
public class XConfig {
    private final Logger logger = LoggerFactory.getLogger(XConfig.class);

    private static XConfigContext xConfigContext;
    private static XKeyObservable xKeyObservable;

    private String project;
    private String zkConn;
    private String profile;
    private String userName;
    private String password;
    private String xconfigDir;//xconfig的默认目录
    private String localConfigDir;//当前配置目录

    public void init() throws XConfigException{
        this.xconfigDir = System.getProperty("user.home")
                + File.separator + Constants.LOCAL_FILE_DIR_NAME;

        //获取profile
        File cfFile = new File(this.xconfigDir + File.separator + Constants.CONFIG_FILE);
        Properties xconfigProp = new Properties();
        try {
            xconfigProp.load(new FileInputStream(cfFile));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(),e);
            throw new XConfigException("无法获取到配置文件，请确认是否存在："+cfFile);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            throw new XConfigException("读取配置文件失败："+cfFile);
        }
        this.profile = xconfigProp.getProperty("profile");
        if(StringUtils.isBlank(this.profile)){
            throw new XConfigException("无法获取到profile信息！");
        }

        if(StringUtils.isBlank(this.zkConn)){
            this.zkConn = xconfigProp.getProperty("zkConn");
        }
        if(StringUtils.isBlank(this.userName) && StringUtils.isBlank(this.password)){
            this.userName = xconfigProp.getProperty("userName");
            this.password = xconfigProp.getProperty("password");
        }

        //生成当前配置目录
        this.localConfigDir = this.xconfigDir + File.separator + this.project + "_"+this.profile;
        //创建文件目录
        File fileDir = new File(this.localConfigDir);
        if(!fileDir.exists()){
            if(!fileDir.mkdirs()){
                throw new XConfigException("无法创建目录："+this.localConfigDir);
            }
        }

        xKeyObservable = new XKeyObservable();
        xConfigContext = new XConfigContext(this,xKeyObservable);
    }

    public static void addObserver(XKeyObserver observer){
        xKeyObservable.addObserver(observer);
    }

    public static void removeObserver(XKeyObserver observer){
        xKeyObservable.removeObserver(observer);
    }

    /**
     * 获取配置value
     * @param key
     * @return 不存在的配置项或者已删除的配置项会返回null
     */
    public static String getValue(String key){
        return xConfigContext.getValue(key);
    }

    /**
     * 获取配置value
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static String getValue(String key,String defaultValue){
        return xConfigContext.getValue(key) == null ? defaultValue : xConfigContext.getValue(key);
    }

    //setter getter
    public Properties getProperties(){
        return xConfigContext.getProperties();
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProfile() {
        return profile;
    }

    public String getXconfigDir() {
        return xconfigDir;
    }

    public String getLocalConfigDir() {
        return localConfigDir;
    }

    public String getZkConn() {
        return zkConn;
    }

    public void setZkConn(String zkConn) {
        this.zkConn = zkConn;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
