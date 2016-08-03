package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.dao.mapper.XUserMapper;
import com.zebra.xconfig.server.po.UserPo;
import com.zebra.xconfig.server.service.XUserService;
import com.zebra.xconfig.server.util.UserUtil;
import com.zebra.xconfig.server.vo.Pagging;
import com.zebra.xconfig.server.vo.UserVo;
import com.zebra.xconfig.server.vo.XUserVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        userVo.setUserNike(userPo.getUserNike());
        userVo.setRole(userPo.getRole());
        userVo.setTimeMillis(System.currentTimeMillis());
        userVo.setSecurity(UserUtil.genSecurityKey(userPo.getUserName(),userPo.getPassword(),userVo.getTimeMillis(),userPo.getSalt()));

        return userVo;
    }

    @Override
    public Pagging<XUserVo> queryUsersByUserName(String userName, int pageNum, int pageSize) {
        userName = StringUtils.isBlank(userName) ? null : userName;
        List<UserPo> userPos = this.xUserMapper.queryByUserNamePagging(userName,pageNum*pageSize,pageSize);
        int count = this.xUserMapper.counByUserName(userName);

        List<XUserVo> xUserVos = new ArrayList<>();
        for(UserPo userPo : userPos){
            XUserVo xUserVo = new XUserVo();
            xUserVo.setUserName(userPo.getUserName());
            xUserVo.setUserNike(userPo.getUserNike());
            xUserVo.setRole(userPo.getRole());
            xUserVo.setCreateTime(CommonUtil.date2String(userPo.getCreateTime()));

            xUserVos.add(xUserVo);
        }

        Pagging<XUserVo> pagging = new Pagging<>();
        pagging.setPageNum(pageNum);
        pagging.setPageSize(pageSize);
        pagging.setT(xUserVos);
        pagging.setCount(count);

        return pagging;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(String userName, String password, String userNike, int role) throws Exception {
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(userNike)) {
            throw new XConfigException("用户名（email），密码，昵称不能为空");
        }

        if(!CommonUtil.checkUserName(userName)){
            throw new XConfigException("用户名（email）格式不正确");
        }

        if(role != 10 && role != 20){
            throw new XConfigException("role不合法");
        }

        UserPo one = this.xUserMapper.loadUser(userName);
        if(one != null){
            throw new XConfigException("当前用户名已经存在");
        }

        String salt = RandomStringUtils.random(10,true,true);
        password = UserUtil.genShaPassword(userName,password,salt);

        UserPo userPo = new UserPo();
        userPo.setUserName(userName);
        userPo.setUserNike(userNike);
        userPo.setPassword(password);
        userPo.setSalt(salt);
        userPo.setRole(role);

        this.xUserMapper.insertUser(userPo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeUser(String userName) throws Exception {
        this.xUserMapper.deleteUserByUserName(userName);
        this.xUserMapper.deleteUserProjectRoleByUserName(userName);
    }
}
