package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2019/1/17
 *   @Discription:
**/
public enum PriceTypeEnum {
//(0,谢谢参与)(1,影视金句)(2,微信号)(3,礼品)(4,)(5,50TFC)(6,200TFC)(7,5000TFC)
    THANK(0),//谢谢参与
    WORD(1),//影视金句
    WE_CHAT(2),//微信号
    GIFT(3),//礼品
    MOVIE_TICKET(4),//电影票
    FIFTY(5),//500TFC
    TWO_HUNDRED(6),//200TFC
    FIVE_THOUSAND(7),//5000TFC
    ;

    private Integer code;

    public Integer code() {
        return code;
    }

    PriceTypeEnum(Integer code) {
        this.code = code;
    }
}
