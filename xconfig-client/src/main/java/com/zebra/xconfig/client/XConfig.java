package com.zebra.xconfig.client;

import java.util.Properties;

/**
 * Created by ying on 16/7/15.
 */
public class XConfig {
    private static XConfigContext xConfigContext;
    private static XKeyObservable xKeyObservable;

    private String project;
    private String profile;
    private String zkConnStr;

    public void init(){
        xKeyObservable = new XKeyObservable();
        xConfigContext = new XConfigContext(this,xKeyObservable);
    }

    public static void addObserver(XKeyObserver observer){
        xKeyObservable.addObserver(observer);
    }

    public static void removeObserver(XKeyObserver observer){
        xKeyObservable.removeObserver(observer);
    }

    public static String getValue(String key){
        return xConfigContext.getValue(key);
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

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getZkConnStr() {
        return zkConnStr;
    }

    public void setZkConnStr(String zkConnStr) {
        this.zkConnStr = zkConnStr;
    }
}
