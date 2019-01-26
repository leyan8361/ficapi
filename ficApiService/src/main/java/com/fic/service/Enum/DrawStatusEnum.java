package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date 2019/1/17
 * @Discription:
 **/
public enum DrawStatusEnum {

    UN_BINGO(0),//"未中奖"

    BINGO(1);//,"中奖"

    private Integer code;

    public Integer code() {
        return code;
    }

    DrawStatusEnum(Integer code) {
        this.code = code;
    }
}
