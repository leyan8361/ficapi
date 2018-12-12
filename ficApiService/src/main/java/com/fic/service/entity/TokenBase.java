package com.fic.service.entity;

import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription:
**/
public class TokenBase {
    private Integer userId;

    private String tokenValue;

    private Date tokenDate;

    private String ipAddress;

    private String userAgent;

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

    public Date getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(Date tokenDate) {
        this.tokenDate = tokenDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public TokenBase(Integer userId, String tokenValue, Date tokenDate, String ipAddress, String userAgent) {
        this.userId = userId;
        this.tokenValue = tokenValue;
        this.tokenDate = tokenDate;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public TokenBase() {
    }
}