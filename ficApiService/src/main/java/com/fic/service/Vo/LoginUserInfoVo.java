package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date 2018/11/22
 * @Discription: 登录入参 For Api
 **/
@ApiModel
public class LoginUserInfoVo {

    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户名ID")
    private Integer userId;
    @ApiModelProperty(value = "Token Value")
    private String tokenValue;
    @ApiModelProperty(value = "用户头像")
    private String himageUrl;
    @ApiModelProperty(value = "邀请码")
    private String myInviteCode;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getHimageUrl() {
        return himageUrl;
    }

    public void setHimageUrl(String himageUrl) {
        this.himageUrl = himageUrl;
    }

    public String getMyInviteCode() {
        return myInviteCode;
    }

    public void setMyInviteCode(String myInviteCode) {
        this.myInviteCode = myInviteCode;
    }
}
