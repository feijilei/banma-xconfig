package com.zebra.xconfig.client;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Created by ying on 16/7/18.
 */
public class XConfigPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    public void setXConfig(XConfig xConfig){
        this.setProperties(xConfig.getProperties());
    }
}
