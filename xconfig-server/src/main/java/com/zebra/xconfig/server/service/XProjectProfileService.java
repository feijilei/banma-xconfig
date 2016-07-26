package com.zebra.xconfig.server.service;

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

    public void insertDepenedencies(String project,Set<String> deps) throws Exception;
}
