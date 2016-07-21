package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.server.dao.mapper.XKvMapper;
import com.zebra.xconfig.server.dao.mapper.XProjectProfileMapper;
import com.zebra.xconfig.server.po.KvPo;
import com.zebra.xconfig.server.service.XKvService;
import com.zebra.xconfig.server.vo.KvVo;
import com.zebra.xconfig.server.vo.Pagging;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ying on 16/7/19.
 */
@Service
public class XkvServiceImpl implements XKvService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private XKvMapper xKvMapper;
    @Autowired
    private XProjectProfileMapper xProjectProfileMapper;
    @Resource
    private CuratorFramework client;

    @Override
    public List<KvPo> queryByProjectAndProfile(String project,String profile) {
        return this.xKvMapper.queryByProjectAndProfile(project, profile);
    }

    @Override
    public Pagging<KvPo> queryByProjectAndProfilePagging(String project, String profile, int pageNum, int pageSize) {
        return null;
    }

    @Override
    @Transactional
    public void addKv(KvPo kvPo) throws Exception {
        if(!CommonUtil.checkName(kvPo.getProject())
                || !CommonUtil.checkName(kvPo.getProfile())
                || !CommonUtil.checkName(kvPo.getxKey())
        ){
            throw new IllegalArgumentException("project，profile，key只能以字母开头，只允许包含字母，数字，点，中划线，下划线");
        }

        this.xKvMapper.addOne(kvPo);

        client.create().forPath(CommonUtil.genKeyPath(kvPo.getProject(),kvPo.getProfile(),kvPo.getxKey()),kvPo.getxValue().getBytes());
    }

    @Override
    @Transactional
    public void updateKv(KvPo kvPo) throws Exception {
        if(!CommonUtil.checkName(kvPo.getProject())
                || !CommonUtil.checkName(kvPo.getProfile())
                || !CommonUtil.checkName(kvPo.getxKey())
                ){
            throw new IllegalArgumentException("project，profile，key只能以字母开头，只允许包含字母，数字，点，中划线，下划线");
        }

        KvPo kv = this.xKvMapper.load(kvPo.getProject(),kvPo.getProfile(),kvPo.getxKey());

        if(kv == null){
            throw new IllegalArgumentException("无法查询到待更新key，请确认是否存在");
        }

        kv.setxValue(kvPo.getxValue());
        kv.setSecurity(kvPo.getSecurity());
        kv.setDescription(kvPo.getDescription());

        this.xKvMapper.updateOne(kv);

        client.setData().forPath(CommonUtil.genKeyPath(kv.getProject(),kv.getProfile(),kv.getxKey()),kv.getxValue().getBytes());
    }
}
