package com.zebra.xconfig.server.service;

import com.zebra.xconfig.server.vo.Pagging;
import com.zebra.xconfig.server.vo.UserVo;
import com.zebra.xconfig.server.vo.XUserVo;

import java.util.List;

/**
 * Created by ying on 16/8/1.
 */
public interface XUserService {
    public UserVo checkUserAndPassword(String userName,String password) throws Exception;

    public Pagging<XUserVo> queryUsersByUserName(String userName,int pageNum,int pageSize);

    public void addUser(String userName,String password,String userNike,int role) throws Exception;

    public void removeUser(String userName) throws Exception;

    public List<String> queryGuestUser(String userNamePre) throws Exception;

    public List<XUserVo> queryProjectOwner(String project);

    /**
     * 增加某一用户在某项目中的角色，目前角色设计，项目设置页面（projectSetting）均是将guest提升为owner所以这里不传role参数
     * @param project
     * @param userName
     */
    public void addUserProjectRole(String project,String userName) throws Exception;

    public void removeUserProjectRole(String project,String userName) throws  Exception;

    public void updateUserNikeAndPassword(String userName,String userNike,String oldPassword,String newPassword) throws Exception;
}
