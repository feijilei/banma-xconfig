package com.zebra.xconfig.server.vo;

/**
 * Created by ying on 16/8/3.
 */
public class XUserVo {
    private String userName;
    private String userNike;
    private String createTime;
    private int role;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNike() {
        return userNike;
    }

    public void setUserNike(String userNike) {
        this.userNike = userNike;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
