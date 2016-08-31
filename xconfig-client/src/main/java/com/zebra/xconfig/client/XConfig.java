package com.zebra.xconfig.client;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.common.exception.XConfigException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ying on 16/7/15.
 */
public class XConfig {
    private final Logger logger = LoggerFactory.getLogger(XConfig.class);

    private static Map<String,XConfigContext> contexts = new HashMap<>();
    private String project;
    private String localConfigDir;//当前配置目录

    private XConfigInitListener xConfigInitListener;

    protected XConfig(String project,XConfigInitListener xConfigInitListener) throws XConfigException{
        this.project = project;
        this.xConfigInitListener = xConfigInitListener;
        //生成当前配置目录
        this.localConfigDir = XConfigFactory.getXconfigDir() + File.separator + this.project + "_"+XConfigFactory.getProfile();
        //创建文件目录
        File fileDir = new File(this.localConfigDir);
        if(!fileDir.exists()){
            if(!fileDir.mkdir()){
                throw new XConfigException("无法创建目录："+this.localConfigDir);
            }
        }
        //创建BootHis目录
        File bootHisDir = new File(this.localConfigDir+File.separator+Constants.LOCAL_BOOT_HIS_DIR);
        if(!bootHisDir.exists()){
            if(!bootHisDir.mkdir()){
                throw new XConfigException("无法创建目录："+bootHisDir.getAbsolutePath());
            }
        }

        XConfigContext xConfigContext = new XConfigContext(this,new XKeyObservable());
        for(String depProject : xConfigContext.getCacheDepProject().keySet()){
            contexts.put(depProject,xConfigContext);
        }

        if(this.xConfigInitListener != null){
            this.xConfigInitListener.complete(this);
        }
    }


    /**
     * @throws XConfigException
     */
    protected XConfig(String project) throws XConfigException{
        this(project,null);
    }


    public static void addObserver(XKeyObserver observer){
        XConfigContext xConfigContext = contexts.get(CommonUtil.genProjectByMkey(observer.getKey()));
        if(xConfigContext == null){

        }else{
            xConfigContext.getxKeyObservable().addObserver(observer);
        }
    }

    public static void removeObserver(XKeyObserver observer){
        XConfigContext xConfigContext = contexts.get(CommonUtil.genProjectByMkey(observer.getKey()));
        if(xConfigContext == null){

        }else{
            xConfigContext.getxKeyObservable().removeObserver(observer);
        }
    }

    /**
     * 获取配置value
     * @param key
     * @return 不存在的配置项或者已删除的配置项会返回null
     */
    public static String getValue(String key){
        XConfigContext xConfigContext = contexts.get(CommonUtil.genProjectByMkey(key));
        if(xConfigContext == null){
            return null;
        }else{
            return xConfigContext.getValue(key);
        }

    }

    /**
     * 获取配置value
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static String getValue(String key,String defaultValue){
        String value = getValue(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 获取当前xConfig对应的Properties对象
     * @return
     */
    public Properties getProperties(){
        XConfigContext xConfigContext = contexts.get(this.getProject());
        return xConfigContext.getProperties();
    }

    public String getProject() {
        return project;
    }

    public String getLocalConfigDir() {
        return localConfigDir;
    }
}
