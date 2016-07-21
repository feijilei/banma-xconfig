package com.zebra.xconfig.server.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ying on 16/7/19.
 */
public interface XProjectProfileMapper {
    public List<String> queryAllProjects();
    public List<String> queryProjectDependencies(@Param("project")String project);
    public List<String> queryProjectProfiles(@Param("project")String project);
}
