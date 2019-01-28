package com.fic.service.Enum;

/**
 * 微信退款状态
 */
public enum WxRefundStatusEnum {


    PROCESSING(0,"PROCESSING"),//退款处理中
    SUCCESS(1,"SUCCESS"),//退款成功
    CHANGE(2,"CHANGE"),//退款异常
    REFUNDCLOSE(3,"REFUNDCLOSE");//退款关闭

    private Integer code;

    private String value;


    WxRefundStatusEnum(Integer code, String value) {
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
