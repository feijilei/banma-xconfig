package com.zebra.xconfig.common;

import org.apache.commons.lang3.StringUtils;

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

    /**
     * zk路径转为mkey值
     * @param path
     * @return
     */
    public static String genMKey(String path){
        return path.substring(1,path.length())
                .replaceFirst("/",".")
                .replaceFirst("/", ".");
    }

    public static String genMKeyPath(String project, String profile, String key){
        return new StringBuilder("/")
                .append(project)
                .append("/")
                .append(profile)
                .append("/")
                .append(key)
                .toString();
    }

    public static String genMKeyPath(String mKey){
        return "/"+mKey.replaceFirst(".","/").replaceFirst(".","/");
    }

    /**
     * 根据mkey获取key
     * @param mkey
     * @return
     */
    public static String genKeyByMkey(String mkey){
        String key = mkey;

        key = key.substring(key.indexOf(".")+1,key.length());
        key = key.substring(key.indexOf(".")+1,key.length());
        return key;
    }

    public static String genProjectByMkey(String mkey){

        return mkey.substring(0,mkey.indexOf("."));
    }

    public static String genProfileByMkey(String mkey){
        String key = mkey;

        key = key.substring(key.indexOf(".")+1,key.length());
        key = key.substring(0,key.indexOf("."));
        return key;
    }

    public static boolean checkName(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        Pattern pattern = Pattern.compile(Constants.NAME_CHECK_REGEX);
        Matcher m = pattern.matcher(name);
        return m.find();
    }

}
