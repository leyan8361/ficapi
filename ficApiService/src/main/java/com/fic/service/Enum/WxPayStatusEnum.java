package com.fic.service.Enum;

/**
 * 微信支付状态
 */
public enum WxPayStatusEnum {

    NOTPAY(0,"NOTPAY"),
    SUCCESS(1,"SUCCESS"),
    USERPAYING(2,"USERPAYING"),
    PAYERROR(3,"PAYERROR"),
    REFUND(4,"REFUND"),
    CLOSED(5,"CLOSED");

    private Integer code;

    private String value;


    WxPayStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer code() {
        return code;
    }

    public String value() {
        return value;
    }
}
