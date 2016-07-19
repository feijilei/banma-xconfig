package com.zebra.xconfig.common;

/**
 * Created by ying on 16/7/15.
 */
public class Constants {
    public static final String NAME_SPACE = "xConfig";//zk 命名空间
    public static final String LOCAL_FILE_DIR_NAME = ".xconfig";//本地默认文件夹
    public static final String DEFAULT_FILE = "local.properties";//默认启动本地模式配置文件

    public static final String CURRENT_FILE = "current.properties";//系统当前使用的配置文件
    public static final String BOOT_FILE = "boot.properties";//系统当前启动时候使用的配置文件

    public static final String LEADER_SELECT_PATH = "/_leaderSelector";//选举路径
    public static final long SYN_PERIOD_MILLIS = 1000*60*10;//多长时间同步一次
}
