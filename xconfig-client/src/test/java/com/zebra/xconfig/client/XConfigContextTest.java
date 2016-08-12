package com.zebra.xconfig.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by ying on 16/7/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
public class XConfigContextTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MysqlConf mysqlConf;
    @Resource
    private XConfig xConfig;

    @Test
    public void test(){
        logger.debug("====>profile:{}",xConfig.getProfile());
        logger.debug("====>{}",XConfig.getValue("mysql.jdbc.password"));
        logger.debug("====>{}", JSON.toJSONString(mysqlConf));

        XConfig.addObserver(new XKeyObserver() {
            @Override
            public String getKey() {
                return "mysql.jdbc.password";
            }

            @Override
            public void change(String value) {
                logger.debug("===change===>{}:{}",getKey(),value);
            }
        });
        try {
            Thread.sleep(1000*60*30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
