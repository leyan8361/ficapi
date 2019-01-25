package com.fic.service.Enum;

/**
 * 微信支付状态
 */
public enum WxPayStatusEnum {

    NOTPAY(0),
    SUCCESS(1),
    USERPAYING(2),
    PAYERROR(3),
    REFUND(4),
    CLOSED(5);

    private Integer code;

    WxPayStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }
}
