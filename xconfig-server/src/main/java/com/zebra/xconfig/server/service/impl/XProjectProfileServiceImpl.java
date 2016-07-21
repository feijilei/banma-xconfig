package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.server.dao.mapper.XProjectProfileMapper;
import com.zebra.xconfig.server.service.XProjectProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
