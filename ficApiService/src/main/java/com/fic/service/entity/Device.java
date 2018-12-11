package com.fic.service.entity;

import java.util.Date;

public class Device {
    private Integer id;

    private String deviceCode;

    private Date createdTime;

    private Integer loginCount;

    private Date lastedLoginTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Date getLastedLoginTime() {
        return lastedLoginTime;
    }

    public void setLastedLoginTime(Date lastedLoginTime) {
        this.lastedLoginTime = lastedLoginTime;
    }
}