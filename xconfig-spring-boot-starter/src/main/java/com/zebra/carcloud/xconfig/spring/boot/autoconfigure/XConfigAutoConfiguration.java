package com.zebra.carcloud.xconfig.spring.boot.autoconfigure;

import com.zebra.xconfig.client.XConfig;
import com.zebra.xconfig.client.XConfigFactory;
import com.zebra.xconfig.common.exception.XConfigException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

/**
 * Created by huachao on 18/01/2017.
 */

@Configuration
@ConditionalOnClass({XConfig.class})
@EnableConfigurationProperties(XConfigProperties.class)
public class XConfigAutoConfiguration  implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static String project;

    @Bean
    @ConditionalOnProperty(prefix = "xconfig", name = "project")
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws XConfigException {
        XConfig xConfig = XConfigFactory.instance(project);
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setProperties(xConfig.getProperties());
        return propertySourcesPlaceholderConfigurer;
    }


    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        for(PropertySource<?> source :  event.getEnvironment().getPropertySources()){
            if(source.getName().equals("applicationConfigurationProperties")){
                if (source instanceof EnumerablePropertySource) {
                    for(String name : ((EnumerablePropertySource) source).getPropertyNames()){
                        if (name.equals("xconfig.project")) {
                            project = ((EnumerablePropertySource) source).getProperty(name).toString();
                        }
                    }
                }
            }
        }
    }

}
