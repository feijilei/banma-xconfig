package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.exception.IllegalNameException;
import com.zebra.xconfig.server.dao.mapper.XProjectProfileMapper;
import com.zebra.xconfig.server.po.ProjectDependency;
import com.zebra.xconfig.server.service.XProjectProfileService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void insertDepenedencies(String project,Set<String> deps) throws Exception{
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
}
