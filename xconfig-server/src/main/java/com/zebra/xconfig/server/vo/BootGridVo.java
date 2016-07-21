package com.zebra.xconfig.server.vo;

import java.util.List;

/**
 * Created by ying on 16/7/20.
 */
public class BootGridVo<T> {
    private int current;
    private int rowCount;
    private int total;
    private List<T> rows;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<T> getRows() {
        return rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
