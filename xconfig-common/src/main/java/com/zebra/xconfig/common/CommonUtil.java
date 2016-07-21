package com.zebra.xconfig.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String genProjectPath(String project){
        StringBuilder sb = new StringBuilder("/")
                .append(project);

        return sb.toString();
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

    public static boolean checkName(String name){
        Pattern pattern = Pattern.compile(Constants.NAME_CHECK_REGEX);
        Matcher m = pattern.matcher(name);
        return m.find();
    }
}
