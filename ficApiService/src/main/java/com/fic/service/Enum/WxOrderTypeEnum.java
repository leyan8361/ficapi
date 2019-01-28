package com.fic.service.Enum;


public enum WxOrderTypeEnum {

    PAY(0,"PAY"),//预支付
    REFUND(1,"REFUND");//退款申请

    private Integer code;

    private String value;


    WxOrderTypeEnum(Integer code, String value) {
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
