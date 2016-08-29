package com.zebra.xconfig.common;

/**
 * Created by ying on 16/7/15.
 */
public class Constants {
    public static final String NAME_SPACE = "xConfig";//zk 命名空间
    public static final String LOCAL_FILE_DIR_NAME = ".xconfig";//本地默认文件夹
    public static final String LOCAL_BOOT_HIS_DIR = "bootHis";//每次启动时候配置文件备份
    public static final String CONFIG_FILE = "config.properties";//xconfig的配置文件
    public static final String DEFAULT_FILE = "local.properties";//默认启动本地模式配置文件

    public static final String CURRENT_FILE = "current.properties";//系统当前使用的配置文件
    public static final String BOOT_FILE = "boot.properties";//系统当前启动时候使用的配置文件

    public static final String LEADER_SELECT_PATH = "/_leaderSelector";//选举路径
    public static final String PROJECT_DEPENDENCY_PATH = "/_dependencies";//项目依赖节点路径 project/_dependencies
    public static final String CLIENT_REGIST_PATH = "/_client";//client注册路径
    public static final long SYN_PERIOD_MILLIS = 1000*60*20;//多长时间同步一次

    public static final String NAME_CHECK_KEY_REGEX = "^[A-Za-z][A-Za-z0-9-_.]*[A-Za-z0-9]$";//project profile key 校验
    public static final String NAME_CHECK_PRO_REGEX = "^[A-Za-z][A-Za-z0-9-_]*[A-Za-z0-9]$";//project profile 校验
    public static final String USERNAME_CHECK = "^[A-Za-z][A-Za-z0-9-_.@]*$";//用户名校验

    public static final String DEFUALT_PROFILE = "dev";
}
