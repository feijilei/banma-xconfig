package com.zebra.xconfig.server.dao.mapper;

import com.zebra.xconfig.server.po.UserPo;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ying on 16/8/1.
 */
public interface XUserMapper {
    public UserPo loadUser(@Param("userName")String userName);
    public Integer loadUserProjectRole(@Param("userName")String userName,@Param("project")String project);
}
