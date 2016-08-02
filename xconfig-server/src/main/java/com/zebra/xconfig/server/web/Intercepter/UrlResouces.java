package com.zebra.xconfig.server.web.Intercepter;

import java.util.Map;

/**
 * Created by ying on 16/8/2.
 */
public class UrlResouces {
    private static Map<String,Integer> resources;

    public static int getResouceRole(String path){
        int role = resources.get(path) == null ? 99999999 :resources.get(path);
        return role;
    }

    public void setResources(Map<String, Integer> resources) {
        UrlResouces.resources = resources;
    }
}
