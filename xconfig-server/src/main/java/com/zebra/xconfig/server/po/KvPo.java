package com.zebra.xconfig.server.po;

import java.util.Date;

/**
 * Created by ying on 16/7/18.
 */
public class KvPo {
    private long id;
    private String project;
    private String profile;
    private String xKey;
    private String xValue;
    private String security;
    private String description;
    private Date createTime;
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProject() {
        return project.trim();
    }

    public void setProject(String project) {
        this.project = project.trim();
    }

    public String getProfile() {
        return profile.trim();
    }

    public void setProfile(String profile) {
        this.profile = profile.trim();
    }

    public String getxKey() {
        return xKey.trim();
    }

    public void setxKey(String xKey) {
        this.xKey = xKey.trim();
    }

    public String getxValue() {
        return xValue.trim();
    }

    public void setxValue(String xValue) {
        this.xValue = xValue.trim();
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
