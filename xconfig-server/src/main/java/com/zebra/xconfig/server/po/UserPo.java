package com.zebra.xconfig.server.po;

import java.util.Date;

/**
 * Created by ying on 16/8/1.
 */
public class UserPo {
    private int id;
    private String userName;
    private String userNike;
    private String password;
    private String salt;
    private Date reateTime;
    private int role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getReateTime() {
        return reateTime;
    }

    public void setReateTime(Date reateTime) {
        this.reateTime = reateTime;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
