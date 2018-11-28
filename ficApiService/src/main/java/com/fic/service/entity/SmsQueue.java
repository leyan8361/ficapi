package com.fic.service.entity;

import java.util.Date;

/**
 * /**
  *   @Author Xie
  *   @Date 2018/11/28
  *   @Discription:
 **/
public class SmsQueue {

    private Integer id;

    private String telephone;

    private String code;

    private Date createdTime;

    private Date expiredTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public SmsQueue(String telephone, String code) {
        this.telephone = telephone;
        this.code = code;
    }

    public SmsQueue() {
    }
}