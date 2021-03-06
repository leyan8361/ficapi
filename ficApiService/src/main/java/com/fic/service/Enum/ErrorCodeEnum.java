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
    USER_HEAD_PIC_UPLOAD_FAILED(1019,"UPLOAD FAILED,SYSTEM_ERROR"),
    USER_PAY_PASSWORD_NOT_SET(1020,"USER_PAY_PASSWORD_NOT_SET"),
    USER_PAY_PASSWORD_NOT_MATCH(1021,"USER_PAY_PASSWORD_NOT_MATCH"),
    NOT_A_TELEPHONE(1022,"NOT_A_TELEPHONE"),
    DEVICE_EXCEPTION(1023,"DEVICE_EXCEPTION"),
    USER_AUTH_FRONT_FACE_MISSED(1024,"USER_AUTH_FRONT_FACE_MISSED"),
    USER_AUTH_BACK_FACE_MISSED(1025,"USER_AUTH_BACK_FACE_MISSED"),
    PIC_ERROR(1026,"ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
    USER_ALREADY_AUTH(1027,"USER_ALREADY_AUTH"),
    USER_NOT_AUTH(1028,"USER_NOT_AUTH"),
    USER_AUTH_ALREADY_APPROVED(1029,"USER_AUTH_ALREADY_APPROVED"),
    TELEPHONE_EXIST(1030,"TELEPHONE_EXIST"),
    TELEPHONE_IS_BEING_USED(1031,"TELEPHONE_IS_BEING_USED"),
    EMAIL_IS_BEING_USED(1032,"EMAIL_IS_BEING_USED"),
    EMAIL_EXIST(1033,"EMAIL_EXIST"),
    NOT_A_EMAIL(1034,"NOT_A_EMAIL"),
    /**
     * 投资相关
     */
    INVEST_NOT_EXIST(2000,"INVEST NOT EXIST"),
    INVEST_BALANCE_NOT_ENOUGH(2001,"INVEST_BALANCE_NOT_ENOUGH"),


    /**
     * 常量费率相关
     */
    EXCHANGE_RATE_MISSED(3000,"EXCHANGE_RATE_MISSED"),
    EXCHANGE_RATE_REPEATED(3001,"EXCHANGE_RATE_REPEATED"),

    /**
     * 版本，更新相关
     */
    VERSION_ILLEGAL(3200,"VERSION ILLEGAL OR UNSUPPORTED DEVICE"),
    VERSION_EXIST(3201,"EXIST"),
    VERSION_NOT_EXIST(3202,"NOT_EXIST"),
    VERSION_FILE_TYPE_NOT_MATCH(3203,"FILE TYPE NOT MATCH"),
    /**
     * 电影相关
     */
    MOVIE_NOT_FOUND(4000,"MOVIE NOT FOUND"),
    BANNER_NOT_FOUND(4001,"BANNER LIST IS EMPTY"),
    MOVIE_EXIST(4002,"MOVIE_EXIST"),

    /**
     * 竞猜相关
     */
    BET_ADD_MOVIE_NOT_FOUND(5000,"BET MOVIE ID NOT FOUND"),
    BET_MOVIE_EXIST(5001,"BET MOVIE NAME EXIST"),
    BET_NO_MOVIE(5002,"NO COULD USED BET MOVIE"),
    BET_BOX_NOT_FOUND(5003,"MOVIE DOES NOT HAS BOX OFFICE"),
    NO_AVALIBLE_SCENCE(5004,"NO_AVALIBLE_SCENCE"),
    THE_MOVIE_NOT_IN_SCENCE(5005,"THE_MOVIE_NOT_IN_SCENCE"),
    SCENCE_MOVIE_NOT_EXIST(5006,"SCENCE_MOVIE_NOT_EXIST"),
    THE_SCENCE_MOVIE_ALREAY_BE_BET(5007,"SCENCE_MOVIE_ALREADY_BET"),
    THE_SCENCE_HAS_NO_MOVIE(5008,"THE_SCENCE_HAS_NO_MOVIE"),
    NO_BET_RECORD(5009,"NO_BET_RECORD"),
    BET_TIME_LOCK(5010,"BET_TIME_LOCK"),
    LUCK_TURNTABLE_NOT_EXIST(5011,"BINGO_PRICE_NOT_EXIST"),
    LUCK_RECORD_NOT_FOUND(5012,"LUCK_RECORD_NOT_FOUND"),
    LUCK_TURNTABLE_IS_BEING_USING(5013,"LUCK_TURNTABLE_IS_BEING_USING"),
    LUCK_TURNTABLE_IS_UN_BINGO(5014,"LUCK_TURNTABLE_IS_UN_BINGO"),
    LUCK_PROBABILITY_OVER_100(5015,"LUCK_PROBABILITY_OVER_100"),
    LUCK_WORD_MISSED(5016,"LUCK_WORD_MISSED"),
    LUCK_ALREADY_RECEIVE(5017,"LUCK_ALREADY_RECEIVE"),
    /**
     * 钱包相关
     */
    WALLET_NOT_EXIST(6000,"WALLET_NOT_EXIST"),
    TRANSACTION_NOT_FOUND(6001,"TRANSACTION_NOT_FOUND"),
    COIN_TYPE_NOT_PERMIT(6002,"WRONG COIN TYPE"),
    TRAN_OUT_NOT_ENOUGH_GAS(6003,"TRAN_OUT_NOT_ENOUGH_GAS"),
    TRAN_FAILED_EXCEPTION(6004,"TRAN_FAILED_EXCEPTION"),
    PAYEE_NOT_EXIST(6005,"PAYEE_NOT_EXIST"),
    TRAN_CAN_NOT_TO_SELF(6006,"TRAN_CAN_NOT_TO_SELF"),
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
