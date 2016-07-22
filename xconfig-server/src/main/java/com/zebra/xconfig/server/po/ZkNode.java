package com.zebra.xconfig.server.po;

/**
 * Created by ying on 16/7/22.
 */
public class ZkNode {
    private String path;
    private String value;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
