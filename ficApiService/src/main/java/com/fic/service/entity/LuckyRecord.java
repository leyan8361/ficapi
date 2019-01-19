package com.fic.service.entity;

import java.util.Date;

public class LuckyRecord {
    private Integer id;

    private Integer userId;

    private Integer bingoPrice;

    private Integer status;

    private Integer isReceive;

    private Date createdTime;

    private Integer trace;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBingoPrice() {
        return bingoPrice;
    }

    public void setBingoPrice(Integer bingoPrice) {
        this.bingoPrice = bingoPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(Integer isReceive) {
        this.isReceive = isReceive;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getTrace() {
        return trace;
    }

    public void setTrace(Integer trace) {
        this.trace = trace;
    }
}