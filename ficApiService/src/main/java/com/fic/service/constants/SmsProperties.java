package com.fic.service.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Xie
 * @Date 2018/11/28
 * @Discription:
 **/
@ConfigurationProperties(prefix = "tencent.sms", ignoreUnknownFields = false)
public class SmsProperties {

    private Integer appId = null;
    private String appKey = "";
    private Integer expired = 5;

    public Integer getExpired() {
        return expired;
    }

    public void setExpired(Integer expired) {
        this.expired = expired;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
