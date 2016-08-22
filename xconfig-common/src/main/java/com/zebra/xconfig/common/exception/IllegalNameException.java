package com.zebra.xconfig.common.exception;

/**
 * Created by ying on 16/7/26.
 */
public class IllegalNameException extends XConfigException{
    public IllegalNameException() {
        super("非法的参数名，key长度最短为2，只能以字母开头，只允许包含字母，数字，点，中划线，下划线，必须以字母或者数字结尾");
    }
}
