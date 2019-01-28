package com.fic.service.Enum;


/**
 * 预订单状态
 */
public enum WxOrderStatusEnum {

    BUILD_ORDER_SUCCESS(0),//创建预支付成功
    BUILD_ORDER_FAILED(1),//创建预支付失败
    SUCCESS(2),//已完成
    CANCEL(3),//取消
    FAILED(4);//支付失败

    private Integer code;

    WxOrderStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }
}
