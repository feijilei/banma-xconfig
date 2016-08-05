package com.zebra.xconfig.server.dao.mapper;

import com.zebra.xconfig.server.po.UserPo;
import com.zebra.xconfig.server.po.UserProjectRolePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ying on 16/8/1.
 */
public interface XUserMapper {
    public UserPo loadUser(@Param("userName")String userName);
    public Integer loadUserProjectRole(@Param("userName")String userName,@Param("project")String project);
    public List<UserPo> queryByUserNamePagging(@Param("userName")String userName,@Param("skip")int skip,@Param("pageSize")int pageSize);
    public Integer counByUserName(@Param("userName")String userName);
    public void insertUser(UserPo userPo);
    public void deleteUserByUserName(@Param("userName")String userName);
    public void deleteUserProjectRoleByUserName(@Param("userName")String userName);
    public List<String> queryGuestUserByUserNameLike(@Param("userName")String userName);
    public List<UserProjectRolePo> queryUserRoleByProject(@Param("project")String project);
    public void insertUserProjectRole(UserProjectRolePo userProjectRolePo);
    public void deleteUserProjectRole(@Param("project")String project,@Param("userName")String userName);
    public void updateUser(@Param("userName")String userName,@Param("userNike")String userNike,@Param("password")String password);
}
