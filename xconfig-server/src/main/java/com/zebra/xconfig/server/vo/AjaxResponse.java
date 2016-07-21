package com.zebra.xconfig.server.vo;

/**
 * Created by ying on 16/7/21.
 */
public class AjaxResponse {
    public boolean isOk;
    public int code;
    public String msg;
    public Object data;

    public AjaxResponse(){
        isOk = true;
    }

    public void setThrowable(Throwable t){
        isOk = false;
        msg = t.getMessage();
    }


    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean isOk) {
        this.isOk = isOk;
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
