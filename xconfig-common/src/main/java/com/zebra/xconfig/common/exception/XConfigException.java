package com.zebra.xconfig.common.exception;

/**
 * Created by ying on 16/7/29.
 */
public class XConfigException extends Exception{
    public XConfigException(String message) {
        super(message);
    }

    public XConfigException(String message,Throwable e){
        super(message,e);
    }
}
