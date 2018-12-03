package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Distribution {
    private Integer id;

    private Integer userId;

    private Date createdTime;

    private BigDecimal inviteRewardOne;

    private BigDecimal investRewardOne;

    private BigDecimal inviteRewardTwo;

    private BigDecimal investRewardTwo;

    private Integer disLevelOneUserId;

    private Integer disLevelTwoUserId;

    private Integer status;

    private Date updatedTime;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public BigDecimal getInviteRewardOne() {
        return inviteRewardOne;
    }

    public void setInviteRewardOne(BigDecimal inviteRewardOne) {
        this.inviteRewardOne = inviteRewardOne;
    }

    public BigDecimal getInvestRewardOne() {
        return investRewardOne;
    }

    public void setInvestRewardOne(BigDecimal investRewardOne) {
        this.investRewardOne = investRewardOne;
    }

    public BigDecimal getInviteRewardTwo() {
        return inviteRewardTwo;
    }

    public void setInviteRewardTwo(BigDecimal inviteRewardTwo) {
        this.inviteRewardTwo = inviteRewardTwo;
    }

    public BigDecimal getInvestRewardTwo() {
        return investRewardTwo;
    }

    public void setInvestRewardTwo(BigDecimal investRewardTwo) {
        this.investRewardTwo = investRewardTwo;
    }

    public Integer getDisLevelOneUserId() {
        return disLevelOneUserId;
    }

    public void setDisLevelOneUserId(Integer disLevelOneUserId) {
        this.disLevelOneUserId = disLevelOneUserId;
    }

    public Integer getDisLevelTwoUserId() {
        return disLevelTwoUserId;
    }

    public void setDisLevelTwoUserId(Integer disLevelTwoUserId) {
        this.disLevelTwoUserId = disLevelTwoUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}