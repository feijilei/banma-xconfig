package com.zebra.xconfig.server.vo;

import java.util.List;

/**
 * Created by ying on 16/7/20.
 */
public class Pagging<T> {
    private int pageNum;//第几页，从0开始
    private int pageSize;//页面大小
    private int pageCount;//页面个数
    private int count;//总共多少条数据
    private List<T> t;

    private void initPageCount(){
        if(pageSize > 0 && count > 0){
            pageCount = count%pageSize > 0 ? count/pageSize+1 : count/pageSize;
        }
    }

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
        this.initPageCount();
    }

    public List<T> getT() {
        return t;
    }

    public void setT(List<T> t) {
        this.t = t;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.initPageCount();
    }

    public int getPageCount() {
        return pageCount;
    }
}
