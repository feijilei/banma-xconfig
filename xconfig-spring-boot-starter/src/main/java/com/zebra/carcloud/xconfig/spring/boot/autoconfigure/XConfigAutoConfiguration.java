package com.zebra.carcloud.xconfig.spring.boot.autoconfigure;

import com.zebra.xconfig.client.XConfig;
import com.zebra.xconfig.client.XConfigFactory;
import com.zebra.xconfig.common.exception.XConfigException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

/**
 * Created by huachao on 18/01/2017.
 */

@Configuration
@ConditionalOnClass({XConfig.class})
@EnableConfigurationProperties(XConfigProperties.class)
public class XConfigAutoConfiguration  implements EnvironmentAware {

    private String project;


    @Bean
    PropertySourcesPlaceholderConfigurer xConfigPropertySourcesPlaceholderConfigurer() throws XConfigException {
        XConfig xConfig = XConfigFactory.instance(project);
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setProperties(xConfig.getProperties());
        return propertySourcesPlaceholderConfigurer;
    }


    /**
     * Set the {@code Environment} that this object runs in.
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        project = environment.getProperty("xconfig.project");
    }
}
