package com.zebra.xconfig.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.Constants;
import com.zebra.xconfig.common.exception.IllegalNameException;
import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.dao.mapper.XKvMapper;
import com.zebra.xconfig.server.dao.mapper.XProjectProfileMapper;
import com.zebra.xconfig.server.dao.mapper.XUserMapper;
import com.zebra.xconfig.server.po.KvPo;
import com.zebra.xconfig.server.po.ProfilePo;
import com.zebra.xconfig.server.po.ProjectDependency;
import com.zebra.xconfig.server.po.ZkNode;
import com.zebra.xconfig.server.service.XProjectProfileService;
import com.zebra.xconfig.server.util.zk.XConfigServer;
import org.apache.commons.collections.CollectionUtils;
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
    @Resource
    private XUserMapper xUserMapper;

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

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void addDepenedencies(String project, Set<String> deps) throws Exception{
        CommonUtil.checkProjectProfileName(project);


        List<ProjectDependency> projectDependencies = new ArrayList<>();
        for(String dep : deps){
            if(StringUtils.isBlank(dep)){
                continue;
            }

            CommonUtil.checkProjectProfileName(dep);

            if(dep.equals(project)){
                throw new XConfigException("不能自己依赖自己");
            }

            String existPro = this.xProjectProfileMapper.loadProject(dep);
            if(StringUtils.isBlank(existPro)){
                throw new XConfigException("不能依赖不存在的project："+dep);
            }

            ProjectDependency projectDependency = new ProjectDependency();
            projectDependency.setProject(project);
            projectDependency.setDepProject(dep);

            projectDependencies.add(projectDependency);
        }

        this.xProjectProfileMapper.delDependencies(project);
        if(projectDependencies.size() > 0) {
            this.xProjectProfileMapper.batchInsertDependencies(projectDependencies);
        }
        xConfigServer.createUpdateKvNode(CommonUtil.genProjectPath(project),projectDependencies.size() > 0 ? StringUtils.join(deps,","):"");

    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void addProfile(ProfilePo profilePo, String source) throws Exception{
        CommonUtil.checkProjectProfileName(profilePo.getProject());
        CommonUtil.checkProjectProfileName(profilePo.getProfile());
        CommonUtil.checkProjectProfileName(source);

        profilePo.setMd5("");

        String targetProject = this.xProjectProfileMapper.loadProject(profilePo.getProject());
        if(StringUtils.isBlank(targetProject)){
            throw new XConfigException("找不到目标project");
        }


        this.xProjectProfileMapper.insertProfile(profilePo);
        this.xConfigServer.createUpdateKvNode(CommonUtil.genProfilePath(profilePo.getProject(),profilePo.getProfile()),"");

        if(StringUtils.isNotBlank(source) && !"none".equals(source)){
            String from = this.xProjectProfileMapper.loadProfile(profilePo.getProject(), source);
            if(StringUtils.isBlank(from)) {
                throw new XConfigException("找不到复制源");
            }

            //操作数据库，写入profile和kv
            this.xKvMapper.bathInsertKvsByProfile(profilePo.getProject(),profilePo.getProfile(),source);

            //写入zk
            List<KvPo> kvPos = this.xKvMapper.queryByProjectAndProfile(profilePo.getProject(),profilePo.getProfile());
            List<ZkNode> zkNodes = new ArrayList<>();
            //写kv
            for(KvPo kvPo : kvPos){
                ZkNode zkNode = new ZkNode();
                zkNode.setPath(CommonUtil.genMKeyPath(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey()));
                zkNode.setValue(kvPo.getxValue());

                zkNodes.add(zkNode);
            }

            this.xConfigServer.createKvNodesWithTransaction(zkNodes);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void removeProfile(String project, String profile) throws Exception{
        CommonUtil.checkProjectProfileName(project);
        CommonUtil.checkProjectProfileName(profile);

        List<String> profiles = this.xProjectProfileMapper.queryProjectProfiles(project);
        if(profiles.size() <= 1){
            throw new XConfigException("不能删除最后一个profile");
        }

        if(this.xConfigServer.getClientsIp(project,profile).size() > 0){
            throw new XConfigException("当前profile正在使用中，不允许删除!");
        }

        //删除数据库profile和kv
        this.xProjectProfileMapper.delProfile(project,profile);
        this.xKvMapper.delByProjectAndProfile(project,profile);

        this.xConfigServer.deleteNode(CommonUtil.genProfilePath(project,profile));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void addProject(String project,String[] profiles) throws Exception {
        CommonUtil.checkProjectProfileName(project);

        this.xProjectProfileMapper.insertProject(project);
        if(profiles == null || profiles.length == 0){
            ProfilePo profilePo = new ProfilePo();
            profilePo.setProject(project);
            profilePo.setProfile(Constants.DEFUALT_PROFILE);
            profilePo.setMd5("");
            profilePo.setProfileKey("");
            this.xProjectProfileMapper.insertProfile(profilePo);

            this.xConfigServer.createUpdateKvNode(CommonUtil.genProfilePath(project,profilePo.getProfile()),"");
        }else{
            List<ZkNode> zkNodes = new ArrayList<>(profiles.length);
            ZkNode projectNode = new ZkNode();
            projectNode.setPath(CommonUtil.genProjectPath(project));
            projectNode.setValue("");
            zkNodes.add(projectNode);

            for(String profile : profiles){
                CommonUtil.checkProjectProfileName(profile);

                ProfilePo profilePo = new ProfilePo();
                profilePo.setProject(project);
                profilePo.setProfile(profile);
                profilePo.setMd5("");
                profilePo.setProfileKey("");

                ZkNode zkNode = new ZkNode();
                zkNode.setPath(CommonUtil.genProfilePath(project,profile));
                zkNodes.add(zkNode);

                this.xProjectProfileMapper.insertProfile(profilePo);
            }

            this.xConfigServer.createKvNodesWithTransaction(zkNodes);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void removeProject(String project) throws Exception {
        CommonUtil.checkProjectProfileName(project);

        List<String> projects = this.xProjectProfileMapper.queryProjectsByDepedProject(project);
        if(CollectionUtils.isNotEmpty(projects)){
            throw  new XConfigException("当前项目被其他项目依赖，不允许删除。"+ JSON.toJSONString(projects));
        }

        this.xProjectProfileMapper.delDependencies(project);
        this.xProjectProfileMapper.delProject(project);
        this.xProjectProfileMapper.delProfileByProject(project);
        this.xKvMapper.delByProject(project);
        this.xUserMapper.deleteUserProjectRoleByProject(project);

        this.xConfigServer.deleteNode(CommonUtil.genProjectPath(project));
    }

    public List<String> queryProfilesOrder(){
        return this.xProjectProfileMapper.queryProfilesOrder();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveProfilesOrder(List<String> profiles) throws Exception{
        if(CollectionUtils.isEmpty(profiles)){
            throw new XConfigException("profiles长度不能空");
        }

        for(String profile : profiles){
            CommonUtil.checkProjectProfileName(profile);
        }
        this.xProjectProfileMapper.delProfilesOrder();
        this.xProjectProfileMapper.insertProfilesOrder(profiles);
    }
}
