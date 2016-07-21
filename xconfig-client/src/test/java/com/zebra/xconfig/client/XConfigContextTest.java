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
    @Test
    public void test(){
        logger.debug("====>{}",XConfig.getValue("mysql.daily.jdbc.password"));
        logger.debug("====>{}", JSON.toJSONString(mysqlConf));
        try {
            Thread.sleep(1000*60*30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
