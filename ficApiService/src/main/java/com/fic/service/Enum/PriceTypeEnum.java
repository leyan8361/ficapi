package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2019/1/17
 *   @Discription:
**/
public enum PriceTypeEnum {

    COIN(0),//"币"
    STUFF(1);//"实物"

    private Integer code;

    public Integer code() {
        return code;
    }

    PriceTypeEnum(Integer code) {
        this.code = code;
    }
}
