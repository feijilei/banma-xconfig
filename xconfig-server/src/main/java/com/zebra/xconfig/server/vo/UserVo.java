package com.zebra.xconfig.server.vo;

/**
 * Created by ying on 16/8/1.
 */
public class UserVo {
    private String userName;
    private String userNike;
    private int role;
    private String security;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getSecurity() {
        return security;
    }

    public String getUserNike() {
        return userNike;
    }

    public void setUserNike(String userNike) {
        this.userNike = userNike;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
}
