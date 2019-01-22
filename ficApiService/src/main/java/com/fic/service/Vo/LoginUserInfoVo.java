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
    @ApiModelProperty(value = "用户名ID",example = "0")
    private Integer userId;
    @ApiModelProperty(value = "Token Value")
    private String tokenValue;
    @ApiModelProperty(value = "用户头像")
    private String himageUrl;
    @ApiModelProperty(value = "邀请码")
    private String myInviteCode;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "是否实名，true已实名，false未实名")
    private boolean isAuth = false;
    @ApiModelProperty(value = "是否设置支付密码,true为已设置，false未设置")
    private boolean isSetPayPassword = false;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public boolean isSetPayPassword() {
        return isSetPayPassword;
    }

    public void setSetPayPassword(boolean setPayPassword) {
        isSetPayPassword = setPayPassword;
    }
}
