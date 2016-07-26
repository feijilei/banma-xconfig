package com.zebra.xconfig.server.po;

/**
 * Created by ying on 16/7/26.
 */
public class ProjectDependency {
    private long id;
    private String project;
    private String depProject;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDepProject() {
        return depProject;
    }

    public void setDepProject(String depProject) {
        this.depProject = depProject;
    }
}
