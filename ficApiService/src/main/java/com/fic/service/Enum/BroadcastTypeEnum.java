package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum BroadcastTypeEnum {

    BET(0),//竞猜
    DRAW(1);//抽奖
    private Integer code;

    public Integer code() {
        return code;
    }

    BroadcastTypeEnum(Integer code) {
        this.code = code;
    }
}
