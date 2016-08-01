package com.zebra.xconfig.server.service;

import com.zebra.xconfig.server.vo.UserVo;

/**
 * Created by ying on 16/8/1.
 */
public interface XUserService {
    public UserVo checkUserAndPassword(String userName,String password) throws Exception;

}
