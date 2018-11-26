package com.fic.service.Vo;

import com.fic.service.Enum.ErrorCodeEnum;

import java.io.Serializable;

/**
 * @Author Xie
 * @Date 2018/11/22
 * @Discription: 响应未登录模板
 **/
public class ResponseVo<T> implements Serializable {

    private Integer errorCode;
    private String msg;
    private T data;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseVo(ErrorCodeEnum errorCodeEnum,T data) {
        this.errorCode = errorCodeEnum.getErrorCode();
        this.msg = errorCodeEnum.getMsg();
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "errorCode=" + errorCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
