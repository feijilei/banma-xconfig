package com.zebra.xconfig.server.vo;

import com.alibaba.fastjson.JSON;
import org.springframework.web.util.HtmlUtils;

/**
 * Created by ying on 16/7/19.
 */
public class KvVo {
    private String key;
//    private String mkey;
    private String value;
    private String description;
    private String security;
    private String project;
    private String createTime;
    private String updateTime;


//    public String getMkey() {
//        return mkey;
//    }
//
//    public void setMkey(String mkey) {
//        this.mkey = mkey;
//    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = HtmlUtils.htmlEscape(value);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
