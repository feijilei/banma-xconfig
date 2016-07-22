package com.zebra.xconfig.server.vo;

/**
 * Created by ying on 16/7/21.
 */
public class AjaxResponse {
    /**
     * 0表示成功
     * 其他表示失败，默认-1
     */
    public int code;
    public String msg;
    public Object data;

    public AjaxResponse(){
        code = 0;
    }

    public void setThrowable(Throwable t){
        code = -1 ;
        msg = t.getMessage();
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
