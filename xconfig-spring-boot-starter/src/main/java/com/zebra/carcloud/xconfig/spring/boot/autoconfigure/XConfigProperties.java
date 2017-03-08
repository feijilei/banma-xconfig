package com.zebra.carcloud.xconfig.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by huachao on 18/01/2017.
 */

@ConfigurationProperties(prefix = XConfigProperties.XCONFIG_PREFIX)
public class XConfigProperties {

    public static final String XCONFIG_PREFIX = "xconfig";

    private String project;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
