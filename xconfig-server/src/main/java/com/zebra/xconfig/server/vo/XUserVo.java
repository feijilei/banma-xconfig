package com.zebra.xconfig.server.vo;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.server.po.UserPo;

/**
 * Created by ying on 16/8/3.
 */
public class XUserVo {
    private String userName;
    private String userNike;
    private String createTime;
    private int role;

    public void setUserPo(UserPo userPo){
        this.userName = userPo.getUserName();
        this.userNike = userPo.getUserNike();
        this.createTime = CommonUtil.date2String(userPo.getCreateTime());
        this.role = userPo.getRole();
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
