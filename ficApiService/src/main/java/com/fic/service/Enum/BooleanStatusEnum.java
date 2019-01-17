package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2019/1/17
 *   @Discription:通用 status (0,not ok)(1,ok)
**/
public enum BooleanStatusEnum {

    NO(0),
    YES(1);

    private Integer code;

    BooleanStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }
}
