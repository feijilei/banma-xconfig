package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.exception.IllegalNameException;
import com.zebra.xconfig.server.dao.mapper.XKvMapper;
import com.zebra.xconfig.server.dao.mapper.XProjectProfileMapper;
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
        if(!CommonUtil.checkName(kvPo.getProject())
                || !CommonUtil.checkName(kvPo.getProfile())
                || !CommonUtil.checkName(kvPo.getxKey())
        ){
            throw new IllegalArgumentException("project，profile，key只能以字母开头，只允许包含字母，数字，点，中划线，下划线");
        }

        KvPo one = this.xKvMapper.load(kvPo.getProject(),kvPo.getProfile(),kvPo.getxKey());
        if(one != null){
            throw new IllegalArgumentException("当前key值已存在，不允许重复添加");
        }

        //todo 需要校验对应的project和profile是否存在

       this.xKvMapper.addOne(kvPo);

        xConfigServer.createUpdateKvNode(CommonUtil.genMKeyPath(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey()),kvPo.getxValue());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateKv(KvPo kvPo) throws Exception {
        if(!CommonUtil.checkName(kvPo.getProject())
                || !CommonUtil.checkName(kvPo.getProfile())
                || !CommonUtil.checkName(kvPo.getxKey())
                ){
            throw new IllegalNameException();
        }

        KvPo kv = this.xKvMapper.load(kvPo.getProject(),kvPo.getProfile(),kvPo.getxKey());

        if(kv == null){
            throw new IllegalArgumentException("无法查询到待更新key，请确认是否存在");
        }

        //todo 需要校验对应的project和profile是否存在

        kv.setxValue(kvPo.getxValue());
        kv.setDescription(kvPo.getDescription());

        if(StringUtils.isNotBlank(kvPo.getSecurity())) {
            kv.setSecurity(kvPo.getSecurity());
        }


        this.xKvMapper.updateOne(kv);

        xConfigServer.createUpdateKvNode(CommonUtil.genMKeyPath(kv.getProject(), kv.getProfile(), kv.getxKey()),kv.getxValue());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void addKvs(List<KvPo> kvPos) throws Exception {
        List<ZkNode> zkNodes = new ArrayList<>();
        for(KvPo kvPo : kvPos){
            if(!CommonUtil.checkName(kvPo.getProject())
                    || !CommonUtil.checkName(kvPo.getProfile())
                    || !CommonUtil.checkName(kvPo.getxKey())
                    ){
                throw new IllegalNameException();
            }

            KvPo one = this.xKvMapper.load(kvPo.getProject(),kvPo.getProfile(),kvPo.getxKey());
            if(one != null){
                throw new IllegalArgumentException("当前key值已存在，不允许重复添加");
            }

            this.xKvMapper.addOne(kvPo);

            ZkNode zkNode = new ZkNode();
            zkNode.setPath(CommonUtil.genMKeyPath(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey()));
            zkNode.setValue(kvPo.getxValue());

            zkNodes.add(zkNode);
        }
        xConfigServer.createKvNodesWithTransactioin(zkNodes);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void removeKvByMkey(String mkey) throws Exception {
        if(StringUtils.isBlank(mkey)){
            throw new IllegalArgumentException("mkey不能为空");
        }

        String project = CommonUtil.genProjectByMkey(mkey);
        String profile = CommonUtil.genProfileByMkey(mkey);
        String key = CommonUtil.genKeyByMkey(mkey);

        this.xKvMapper.delOne(project,profile,key);

        xConfigServer.removeNode(CommonUtil.genMKeyPath(project, profile, key));
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
}
