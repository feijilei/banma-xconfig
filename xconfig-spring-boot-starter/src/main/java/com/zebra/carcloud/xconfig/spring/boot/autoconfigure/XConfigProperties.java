package com.zebra.carcloud.xconfig.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by huachao on 18/01/2017.
 */

@Component
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
