package com.zebra.xconfig.server.service;

import com.zebra.xconfig.server.po.ProfilePo;

import java.awt.color.ProfileDataException;
import java.util.List;
import java.util.Set;

/**
 * Created by ying on 16/7/19.
 */
public interface XProjectProfileService {
    public List<String> queryAllProjects();

    public List<String> queryProjectsByPrefix(String projectPre);

    public List<String> queryProjectProfiles(String project);

    public List<String> queryProjectDependencies(String project);

    public void addDepenedencies(String project, Set<String> deps) throws Exception;

    /**
     * 增加profile
     * @param profilePo
     * @param source 根据此源复制创建新的profile
     */
    public void addProfile(ProfilePo profilePo,String source) throws Exception;

    public void removeProfile(String project,String profile) throws Exception;
}
