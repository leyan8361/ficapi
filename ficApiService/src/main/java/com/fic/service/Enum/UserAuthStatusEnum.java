package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2019/1/15
 *   @Discription: 实名认证状态
**/
public enum UserAuthStatusEnum {


    WAIT_APPROVE(0),//等待审核
    APPROVE(1),//审核通过
    REJECT(2);//拒绝通过;

    private int code;

    UserAuthStatusEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
