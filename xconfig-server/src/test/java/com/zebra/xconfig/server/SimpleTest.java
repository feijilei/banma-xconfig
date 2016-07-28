package com.zebra.xconfig.server;

import com.zebra.xconfig.common.CommonUtil;
import org.junit.Test;

/**
 * Created by ying on 16/7/21.
 */
public class SimpleTest {

    @Test
    public void test(){
        System.out.println(CommonUtil.genKey("/mysql/dev/jdbc.drive"));
        System.out.println(CommonUtil.genXKeyByKey("mysql.test"));
    }
}
