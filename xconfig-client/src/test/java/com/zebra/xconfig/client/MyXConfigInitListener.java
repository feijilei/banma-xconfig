package com.zebra.xconfig.client;

/**
 * Created by ying on 16/8/31.
 */
public class MyXConfigInitListener implements XConfigInitListener {
    @Override
    public void complete(XConfig xConfig) {
        System.out.println("====================xconfig is ok============");
    }
}
