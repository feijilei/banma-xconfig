package com.zebra.xconfig.client;

import java.util.Properties;

/**
 * Created by ying on 16/7/15.
 */
public class XConfig {
    private static XConfigContext xConfigContext;

    private String project;
    private String profile;
    private String zkConnStr;

    public void init(){
        xConfigContext = new XConfigContext(this);
    }

    public static String getValue(String key){
        return xConfigContext.getValue(key);
    }

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
