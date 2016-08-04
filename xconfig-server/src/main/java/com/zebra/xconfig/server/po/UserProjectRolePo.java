package com.zebra.xconfig.server.po;

/**
 * Created by ying on 16/8/1.
 */
public class UserProjectRolePo {
    private String userName;
    private String project;
    private int role;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
