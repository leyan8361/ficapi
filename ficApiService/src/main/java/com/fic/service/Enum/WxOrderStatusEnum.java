package com.fic.service.Enum;


/**
 * 预订单状态
 */
public enum WxOrderStatusEnum {

    BUILD_ORDER_SUCCESS(0),
    BUILD_ORDER_FAILED(1),
    SUCCESS(2),
    CANCEL(3),
    FAILED(4);

    private Integer code;

    WxOrderStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }
}
