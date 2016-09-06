package com.zebra.xconfig.server.dao.mapper;

import com.zebra.xconfig.server.po.ProfilePo;
import com.zebra.xconfig.server.po.ProjectDependency;
import com.zebra.xconfig.server.po.ProjectPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ying on 16/7/19.
 */
public interface XProjectProfileMapper {
    public List<String> queryAllProjects();
    public List<ProjectPo> queryAllProjectsPo();
    public List<String> queryProjectDependencies(@Param("project")String project);
    public List<String> queryProjectProfiles(@Param("project")String project);
    public List<String> queryProjectsByPrefix(@Param("prefix")String prefix);
    public void batchInsertDependencies(List<ProjectDependency> projectDependencies);
    public void delDependencies(@Param("project")String project);
    public String loadProject(@Param("project")String project);
    public String loadProfile(@Param("project")String project,@Param("profile")String profile);
    public void insertProfile(ProfilePo profilePo);
    public void delProfile(@Param("project")String project,@Param("profile")String profile);
    public void insertProject(@Param("project")String project,@Param("description")String description);
    public void delProject(@Param("project")String project);
    public List<String> queryProjectsByDepedProject(@Param("depProject")String depProject);
    public void delProfileByProject(@Param("project")String project);
    public List<String> queryProfilesOrder();
    public void insertProfilesOrder(List<String> profiles);
    public void delProfilesOrder();
}
