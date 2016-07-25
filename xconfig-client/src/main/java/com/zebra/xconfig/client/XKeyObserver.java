package com.zebra.xconfig.client;

/**
 * Created by ying on 16/7/25.
 */
public interface XKeyObserver {
    public String getKey();
    public void change(String value);
}
