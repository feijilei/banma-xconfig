package com.zebra.xconfig.common.exception;

/**
 * Created by ying on 16/7/26.
 */
public class IllegalProNameException extends XConfigException{
    public IllegalProNameException() {
        super("非法的参数名，project，profile长度最短为2，只能以字母开头，只允许包含字母，数字，中划线，下划线，必须以字母或者数字结尾");
    }
}
