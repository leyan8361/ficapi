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
