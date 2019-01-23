package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2019/1/17
 *   @Discription:
**/
public enum PriceTypeEnum {
//(0,10TFC)(1,影视金句)(2,微信号)(3,礼品)(4,)(5,50TFC)(6,200TFC)(7,5000TFC)
    TEN(0),
    WORD(1),
    WE_CHAT(2),
    GIFT(3),//礼品
    MOVIE_TICKET(4),
    FIFTY(5),
    TWO_HUNDRED(6),
    FIVE_THOUSAND(7),
    COVER(100)
    ;

    private Integer code;

    public Integer code() {
        return code;
    }

    PriceTypeEnum(Integer code) {
        this.code = code;
    }
}
