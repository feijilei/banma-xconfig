package com.zebra.xconfig.common;

import com.zebra.xconfig.common.exception.IllegalNameException;
import com.zebra.xconfig.common.exception.IllegalProNameException;
import com.zebra.xconfig.common.exception.XConfigException;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ying on 16/7/15.
 *
 * mKey project.profile.key xConfig内部使用 例如 mysql.dev.username
 * xKey key 数据库中存储的xkey字段
 * key  project.key 生成的properties文件中真正的key 例如 mysql.username
 * path /project/profile/key xCofnig向zk注册的路径 例如 /mysql/dev/username
 */
public class CommonUtil {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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

    public static String genMKey(String project,String profile,String xkey){
        StringBuilder sb = new StringBuilder(project)
                .append(".")
                .append(profile)
                .append(".")
                .append(xkey);
        return sb.toString();
    }

    public static String genKey(String project,String xkey){
        StringBuilder sb = new StringBuilder(project)
                .append(".")
                .append(xkey);
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

    /**
     *
     * @param path /mysql/dev/username
     * @return mysql.username
     */
    public static String genKey(String path){
        String[] paths = path.split("/");
        StringBuilder sb = new StringBuilder();
        sb.append(paths[1])
                .append(".")
                .append(paths[3]);

        return sb.toString();
    }

    public static String genMKeyPath(String project, String profile, String xkey){
        return new StringBuilder("/")
                .append(project)
                .append("/")
                .append(profile)
                .append("/")
                .append(xkey)
                .toString();
    }

    public static String genMKeyPath(String mKey){
        return "/"+mKey.replaceFirst(".","/").replaceFirst(".","/");
    }

    /**
     * 根据key获取xkey
     * @param key
     * @return
     */
    public static String genXKeyByKey(String key){
        String[] keys = key.split("\\.");
        return StringUtils.join(keys,".",1,keys.length);
    }

    /**
     * 根据mkey获取xkey
     * @param mkey
     * @return
     */
    public static String genXKeyByMKey(String mkey){
        String[] keys = mkey.split("\\.");
        return StringUtils.join(keys, ".", 2, keys.length);
    }

    public static String genProjectByMkey(String mkey){
        if(mkey.indexOf('.') == -1){
            throw new IllegalArgumentException("不符合规则的key："+mkey);
        }

        return mkey.substring(0,mkey.indexOf("."));
    }

    public static String genProfileByMkey(String mkey){
        String[] strs = mkey.split("\\.");

        return strs[1];
    }

    /**
     * project profile key 名字校验
     * @param name
     * @return
     */
    public static void checkKeyName(String name) throws XConfigException{
        if (StringUtils.isBlank(name)) {
            throw new XConfigException("project profile key名字不能为空");
        }

        if(name.length() > 50){
            throw new XConfigException("project profile key名字长度不能超过50个字符");
        }

        Pattern pattern = Pattern.compile(Constants.NAME_CHECK_KEY_REGEX);
        Matcher m = pattern.matcher(name);
        if(!m.find()){
            throw new IllegalNameException();
        }
    }

    public static void checkProjectProfileName(String name) throws XConfigException{
        if (StringUtils.isBlank(name)) {
            throw new XConfigException("project profile名字不能为空");
        }

        if(name.length() > 50){
            throw new XConfigException("project profile名字长度不能超过50个字符");
        }

        Pattern pattern = Pattern.compile(Constants.NAME_CHECK_PRO_REGEX);
        Matcher m = pattern.matcher(name);
        if(!m.find()){
            throw new IllegalProNameException();
        }
    }

    /**
     *
     * @param value
     * @throws XConfigException
     */
    public static void checkValue(String value) throws XConfigException{
        if (value.length() > 1000){
            throw new XConfigException("value长度不能超过1000个字符");
        }
    }

    /**
     * 校验用户名
     * @param userName
     * @return
     */
    public static boolean checkUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return false;
        }
        Pattern pattern = Pattern.compile(Constants.USERNAME_CHECK);
        Matcher m = pattern.matcher(userName);
        return m.find();
    }

    public static String date2String(Date date){
        if(date == null){
            return "1970-1-1 00:00:00";
        }

        return simpleDateFormat.format(date);
    }

}
