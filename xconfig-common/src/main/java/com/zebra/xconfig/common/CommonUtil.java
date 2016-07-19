package com.zebra.xconfig.common;

/**
 * Created by ying on 16/7/15.
 */
public class CommonUtil {

    public static String genProfilePath(String project,String profile){
        StringBuilder sb = new StringBuilder("/")
                .append(project)
                .append("/")
                .append(profile);

        return  sb.toString();
    }

    public static String genMKey(String project,String profile,String key){
        StringBuilder sb = new StringBuilder(project)
                .append(".")
                .append(profile)
                .append(".")
                .append(key);
        return sb.toString();
    }

    public static String genMKey(String path){
        return path.substring(1,path.length())
                .replaceFirst("/",".")
                .replaceFirst("/", ".");
    }

    public static String genKeyPath(String project,String profile,String key){
        return new StringBuilder("/")
                .append(project)
                .append("/")
                .append(profile)
                .append("/")
                .append(key)
                .toString();
    }
}
