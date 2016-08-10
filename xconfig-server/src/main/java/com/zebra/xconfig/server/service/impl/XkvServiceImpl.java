package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.exception.IllegalNameException;
import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.dao.mapper.XKvMapper;
import com.zebra.xconfig.server.dao.mapper.XProjectProfileMapper;
import com.zebra.xconfig.server.dao.mapper.XUserMapper;
import com.zebra.xconfig.server.po.KvPo;
import com.zebra.xconfig.server.po.ZkNode;
import com.zebra.xconfig.server.service.XKvService;
import com.zebra.xconfig.server.util.zk.XConfigServer;
import com.zebra.xconfig.server.vo.Pagging;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private XConfigServer xConfigServer;

    @Override
    public List<KvPo> queryByProjectAndProfile(String project,String profile) {
        return this.xKvMapper.queryByProjectAndProfile(project, profile);
    }

    @Override
    public Pagging<KvPo> queryByProjectAndProfilePagging(String project, String profile, int pageNum, int pageSize) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void addKv(KvPo kvPo) throws Exception {
        CommonUtil.checkName(kvPo.getProject());
        CommonUtil.checkName(kvPo.getProfile());
        CommonUtil.checkName(kvPo.getxKey());
        CommonUtil.checkValue(kvPo.getxValue());

        this.checkProjectAndProfile(kvPo.getProject(),kvPo.getProfile());

        KvPo one = this.xKvMapper.load(kvPo.getProject(),kvPo.getProfile(),kvPo.getxKey());
        if(one != null){
            throw new XConfigException("当前key值已存在，不允许重复添加");
        }

       this.xKvMapper.addOne(kvPo);

        xConfigServer.createUpdateKvNode(CommonUtil.genMKeyPath(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey()),kvPo.getxValue());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateKv(KvPo kvPo) throws Exception {
        CommonUtil.checkName(kvPo.getProject());
        CommonUtil.checkName(kvPo.getProfile());
        CommonUtil.checkName(kvPo.getxKey());
        CommonUtil.checkValue(kvPo.getxValue());

        this.checkProjectAndProfile(kvPo.getProject(),kvPo.getProfile());

        KvPo kv = this.xKvMapper.load(kvPo.getProject(),kvPo.getProfile(),kvPo.getxKey());
        if(kv == null){
            throw new XConfigException("无法查询到待更新key，请确认是否存在");
        }

        boolean valueChange = true;
        if(kv.getxValue().equals(kvPo.getxValue())){
            valueChange = false;
        }

        kv.setxValue(kvPo.getxValue());
        kv.setDescription(kvPo.getDescription());

        if(StringUtils.isNotBlank(kvPo.getSecurity())) {
            kv.setSecurity(kvPo.getSecurity());
        }


        this.xKvMapper.updateOne(kv);

        if(valueChange) {
            xConfigServer.createUpdateKvNode(CommonUtil.genMKeyPath(kv.getProject(), kv.getProfile(), kv.getxKey()), kv.getxValue());
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void addKvs(List<KvPo> kvPos) throws Exception {
        List<ZkNode> zkNodes = new ArrayList<>();
        for(KvPo kvPo : kvPos){
            CommonUtil.checkName(kvPo.getProject());
            CommonUtil.checkName(kvPo.getProfile());
            CommonUtil.checkName(kvPo.getxKey());
            CommonUtil.checkValue(kvPo.getxValue());

            this.checkProjectAndProfile(kvPo.getProject(),kvPo.getProfile());

            KvPo one = this.xKvMapper.load(kvPo.getProject(),kvPo.getProfile(),kvPo.getxKey());
            if(one != null){
                throw new XConfigException("当前key值已存在，不允许重复添加");
            }

            this.xKvMapper.addOne(kvPo);

            ZkNode zkNode = new ZkNode();
            zkNode.setPath(CommonUtil.genMKeyPath(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey()));
            zkNode.setValue(kvPo.getxValue());

            zkNodes.add(zkNode);
        }
        xConfigServer.createKvNodesWithTransaction(zkNodes);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void removeKvBykey(String profile,String key) throws Exception {
        if(StringUtils.isBlank(key) || StringUtils.isBlank(profile)){
            throw new XConfigException("key,profile不能为空");
        }

        String project = CommonUtil.genProjectByKey(key);
        String xkey = CommonUtil.genXKeyByKey(key);

        this.xKvMapper.delOne(project,profile,xkey);

        xConfigServer.deleteNode(CommonUtil.genMKeyPath(project, profile, xkey));
    }

    @Override
    public List<KvPo> queryByProjectAndProfileWithDeps(String project, String profile) {
        List<String> deps = this.xProjectProfileMapper.queryProjectDependencies(project);
        if(deps == null){
            deps = new ArrayList<>();
        }

        deps.add(project);


        return this.xKvMapper.queryByProjectsAndProfile(deps,profile);
    }

    private void checkProjectAndProfile(String project,String profile) throws XConfigException{
        String pj = this.xProjectProfileMapper.loadProject(project);
        if(StringUtils.isBlank(pj)){
            throw new XConfigException("project不存在："+project);
        }
        String pf = this.xProjectProfileMapper.loadProfile(project,profile);
        if(StringUtils.isBlank(pf)){
            throw new XConfigException("profile不存在："+profile);
        }
    }
}
