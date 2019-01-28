package com.fic.service.Enum;

/**
 * 微信支付状态
 */
public enum WxPayStatusEnum {

    NOTPAY(0,"NOTPAY"),//未支付
    SUCCESS(1,"SUCCESS"),//支付成功
    USERPAYING(2,"USERPAYING"),//用户支付中
    PAYERROR(3,"PAYERROR"),//支付失败
    REFUND(4,"REFUND"),//转入退款
    CLOSED(5,"CLOSED");//已关闭

    private Integer code;

    private String value;


    WxPayStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
//
//    public  static WxPayStatusEnum getCode(String value){
//        switch (value){
//            case "NOTPAY":
//                return WxPayStatusEnum.NOTPAY;
//            case "SUCCESS":
//                return WxPayStatusEnum.SUCCESS;
//            case "USERPAYING":
//                return WxPayStatusEnum.USERPAYING;
//            case "PAYERROR":
//                return WxPayStatusEnum.PAYERROR;
//            case "REFUND":
//                return WxPayStatusEnum.REFUND;
//            case "CLOSED":
//                return WxPayStatusEnum.CLOSED;
//            default:
//                return null;
//        }
//    }

    public Integer code() {
        return code;
    }

    public String value() {
        return value;
    }
}
