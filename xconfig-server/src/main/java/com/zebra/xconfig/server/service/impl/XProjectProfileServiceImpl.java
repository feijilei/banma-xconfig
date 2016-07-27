package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.exception.IllegalNameException;
import com.zebra.xconfig.server.dao.mapper.XKvMapper;
import com.zebra.xconfig.server.dao.mapper.XProjectProfileMapper;
import com.zebra.xconfig.server.po.KvPo;
import com.zebra.xconfig.server.po.ProfilePo;
import com.zebra.xconfig.server.po.ProjectDependency;
import com.zebra.xconfig.server.po.ZkNode;
import com.zebra.xconfig.server.service.XProjectProfileService;
import com.zebra.xconfig.server.util.zk.XConfigServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ying on 16/7/19.
 */
@Service
public class XProjectProfileServiceImpl implements XProjectProfileService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private XProjectProfileMapper xProjectProfileMapper;
    @Autowired
    private XKvMapper xKvMapper;
    @Resource
    private XConfigServer xConfigServer;

    @Override
    public List<String> queryAllProjects() {
        return this.xProjectProfileMapper.queryAllProjects();
    }

    @Override
    public List<String> queryProjectProfiles(String project) {
        return xProjectProfileMapper.queryProjectProfiles(project);
    }

    @Override
    public List<String> queryProjectDependencies(String project) {
        return xProjectProfileMapper.queryProjectDependencies(project);
    }

    @Override
    public List<String> queryProjectsByPrefix(String projectPre) {
        return xProjectProfileMapper.queryProjectsByPrefix(projectPre);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addDepenedencies(String project, Set<String> deps) throws Exception{
        if(!CommonUtil.checkName(project)){
            throw new IllegalNameException();
        }


        List<ProjectDependency> projectDependencies = new ArrayList<>();
        for(String dep : deps){
            if(!CommonUtil.checkName(dep)) {
                throw new IllegalNameException();
            }

            if(dep.equals(project)){
                throw new IllegalArgumentException("不能自己依赖自己");
            }

            String existPro = this.xProjectProfileMapper.loadProject(dep);
            if(StringUtils.isBlank(existPro)){
                throw new IllegalArgumentException("不能依赖不存在的project："+dep);
            }

            ProjectDependency projectDependency = new ProjectDependency();
            projectDependency.setProject(project);
            projectDependency.setDepProject(dep);

            projectDependencies.add(projectDependency);
        }

        this.xProjectProfileMapper.delDependencies(project);
        this.xProjectProfileMapper.batchInsertDependencies(projectDependencies);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProfile(ProfilePo profilePo, String source) throws Exception{
        if(!CommonUtil.checkName(profilePo.getProject()) || !CommonUtil.checkName(profilePo.getProfile()) || !CommonUtil.checkName(source)){
            throw new IllegalNameException();
        }

        profilePo.setMd5("");

        //判断project和source是否存在
        String targetProject = this.xProjectProfileMapper.loadProject(profilePo.getProject());
        String from = this.xProjectProfileMapper.loadProfile(profilePo.getProject(), source);

        if(StringUtils.isBlank(targetProject) || StringUtils.isBlank(from)){
            throw new IllegalArgumentException("找不到目标project或复制源");
        }

        //操作数据库，写入profile和kv
        this.xProjectProfileMapper.insertProfile(profilePo);
        this.xKvMapper.bathInsertKvsByProfile(profilePo.getProject(),profilePo.getProfile(),source);

        //写入zk
        List<KvPo> kvPos = this.xKvMapper.queryByProjectAndProfile(profilePo.getProject(),profilePo.getProfile());
        List<ZkNode> zkNodes = new ArrayList<>();
        //先写profile
        ZkNode profileNode = new ZkNode();
        profileNode.setPath(CommonUtil.genProfilePath(profilePo.getProject(),profilePo.getProfile()));
        profileNode.setValue("");
        zkNodes.add(profileNode);
        //写kv
        for(KvPo kvPo : kvPos){
            ZkNode zkNode = new ZkNode();
            zkNode.setPath(CommonUtil.genMKeyPath(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey()));
            zkNode.setValue(kvPo.getxValue());

            zkNodes.add(zkNode);
        }

        this.xConfigServer.createKvNodesWithTransaction(zkNodes);
    }

    @Override
    public void removeProfile(String project, String profile) throws Exception{
        if(!CommonUtil.checkName(project) || !CommonUtil.checkName(profile)){
            throw new IllegalNameException();
        }

        //删除数据库profile和kv
        this.xProjectProfileMapper.delProfile(project,profile);
        this.xKvMapper.delByProjectAndProfile(project,profile);

        this.xConfigServer.deleteNode(CommonUtil.genProfilePath(project,profile));
    }
}
