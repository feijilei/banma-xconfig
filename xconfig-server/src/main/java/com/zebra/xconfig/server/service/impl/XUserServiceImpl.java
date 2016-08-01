package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.dao.mapper.XUserMapper;
import com.zebra.xconfig.server.po.UserPo;
import com.zebra.xconfig.server.service.XUserService;
import com.zebra.xconfig.server.util.UserUtil;
import com.zebra.xconfig.server.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ying on 16/8/1.
 */
@Service
public class XUserServiceImpl implements XUserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private XUserMapper xUserMapper;

    @Override
    public UserVo checkUserAndPassword(String userName, String password) throws Exception {
        UserPo userPo = this.xUserMapper.loadUser(userName);
        if(userPo == null){
            throw new XConfigException("当前用户不存在");
        }

        String shaPassword = UserUtil.genShaPassword(userPo.getUserName(),password,userPo.getSalt());

        if(!shaPassword.equals(userPo.getPassword())){
            throw new XConfigException("用户名或者密码不正确");
        }

        UserVo userVo = new UserVo();
        userVo.setUserName(userPo.getUserName());
        userVo.setUserNike(userPo.getUserName());
        userVo.setRole(userPo.getRole());
        userVo.setSecurity(UserUtil.genSecurityKey(userPo.getUserName(),userPo.getPassword(),userPo.getSalt()));

        return userVo;
    }
}
