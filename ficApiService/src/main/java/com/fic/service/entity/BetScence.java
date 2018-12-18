package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BetScence {
    private Integer id;

    private String betName;

    private Byte status;

    private Date createdTime;

    private Byte betType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBetName() {
        return betName;
    }

    public void setBetName(String betName) {
        this.betName = betName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Byte getBetType() {
        return betType;
    }

    public void setBetType(Byte betType) {
        this.betType = betType;
    }
}