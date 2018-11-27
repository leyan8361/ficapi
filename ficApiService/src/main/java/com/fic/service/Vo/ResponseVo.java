package com.fic.service.Vo;

import com.fic.service.Enum.ErrorCodeEnum;

import java.io.Serializable;

/**
 * @Author Xie
 * @Date 2018/11/22
 * @Discription: 响应模板
 **/
public class ResponseVo<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ResponseVo(ErrorCodeEnum errorCodeEnum, T data) {
        this.code = errorCodeEnum.getCode();
        this.msg = errorCodeEnum.getMsg();
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
