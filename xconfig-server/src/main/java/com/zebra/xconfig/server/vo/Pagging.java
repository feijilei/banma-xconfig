package com.zebra.xconfig.server.vo;

import java.util.List;

/**
 * Created by ying on 16/7/20.
 */
public class Pagging<T> {
    private int pageNum;
    private int pageSize;
    private int count;
    private List<T> t;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getT() {
        return t;
    }

    public void setT(List<T> t) {
        this.t = t;
    }
}
