package com.fic.service.Enum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date 2018/11/22
 * @Discription: Error Code
 **/
public enum ErrorCodeEnum {

    /**
     * SUCCESS
     */
    @ApiModelProperty
    SUCCESS(200,"SUCCESS"),

    /**
     * Auth
     */
    UNAUTHORIZED(401,"UnLogin Or UnAuthorized"),
    PASSWORD_NOT_MATCH(1000,"Password UnMatch"),
    USER_NOT_EXIST(1001,"User Not Exist"),
    TOKEN_MISSED_HEADER(1002,"Missed Token In Header"),
    UID_ILLEGAL(1003,"USER ID ILLEGAL"),
    TOKEN_NOT_MATCH(1004,"INVALID TOKEN"),
    TOKEN_INVALID(1005,"TOKEN INVALID TIME_OUT"),
    USER_AGENT_NOT_MATCH(1006,"USER AGENT NOT MATCH"),
    USERNAME_EXIST(1007,"USER EXIST"),
    OLD_PASSWORD_NOT_MATCH(1008,"OLD PASSWORD NOT MATCH"),
    RE_PASSWORD_NOT_MATCH(1009,"NEW PASSWORD NOT MATCH WITH RE"),
    TOKEN_NOT_EXIST(1010,"TOKEN NOT EXIST"),
    INVITE_CODE_NOT_EXIST(1011,"INVALID INVITE CODE"),
    INVALID_TELEPHONE(1012,"INVALID TELEPHONE"),
    VALIDATE_CODE_INVALID(1013,"VALIDATE_CODE_INVALID"),
    VALIDATE_CODE_EXPIRED(1014,"VALIDATE_CODE_EXPIRED"),
    SMS_SEND_ERROR_TEMPLATE(1015,"模板未通过审核或内容不匹配"),
    SMS_SEND_COUNT_LIMIT(1016,"SMS_SEND_COUNT_LIMIT"),
//    SMS_SEND_ALL_LIMIT(1016,"业务短信日下发条数超过设定的上限"),
//    SMS_SEND_30S_LIMIT(1017,"单个手机号 30 秒内下发短信条数超过设定的上限"),
//    SMS_SEND_1H_LIMIT(1018,"单个手机号 1 小时内下发短信条数超过设定的上限"),
//    SMS_SEND_1DAY_LIMIT(1019,"单个手机号日下发短信条数超过设定的上限"),
    SMS_SEND_REST_ERROR(1017,"其余错误"),
    USER_HEAD_PIC_ERROR(1018,"ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
    /**
     * 投资相关
     */
    INVEST_NOT_EXIST(2000,"INVEST NOT EXIST"),
    INVEST_BALANCE_NOT_ENOUGH(2001,"INVEST_BALANCE_NOT_ENOUGH"),


    /**
     * 常量费率相关
     */
    EXCHANGE_RATE_MISSED(3000,"EXCHANGE_RATE_MISSED"),

    /**
     * System Error
     */
    SYSTEM_EXCEPTION(500,"System ERROR"),
    PARAMETER_MISSED(400,"Parameter Missed"),
    ;

    private Integer code;
    private String msg;

    ErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ErrorCodeEnum matchCode(Integer code){
        if(null == code)return null;
        for(ErrorCodeEnum errorCode: values()){
            if(errorCode.getCode().equals(code)){
                return errorCode;
            }
        }
        return null;
    }

}
