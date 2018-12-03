package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DistributionRecord {
    private Integer id;

    private Integer userId;

    private BigDecimal registerReward;

    private Date registerTime;

    private Integer inviteFirstUserId;

    private Date inviteFirstUserRegisterTime;

    private BigDecimal inviteRewardFirst;

    private BigDecimal investRewardFirst;

    private Date investTimeFirst;

    private Integer inviteSecondUserId;

    private Date inviteSecondUserRegisterTime;

    private BigDecimal inviteRewardSecond;

    private BigDecimal investRewardSecond;

    private Date investTimeSecond;

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

    public BigDecimal getRegisterReward() {
        return registerReward;
    }

    public void setRegisterReward(BigDecimal registerReward) {
        this.registerReward = registerReward;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getInviteFirstUserId() {
        return inviteFirstUserId;
    }

    public void setInviteFirstUserId(Integer inviteFirstUserId) {
        this.inviteFirstUserId = inviteFirstUserId;
    }

    public Date getInviteFirstUserRegisterTime() {
        return inviteFirstUserRegisterTime;
    }

    public void setInviteFirstUserRegisterTime(Date inviteFirstUserRegisterTime) {
        this.inviteFirstUserRegisterTime = inviteFirstUserRegisterTime;
    }

    public BigDecimal getInviteRewardFirst() {
        return inviteRewardFirst;
    }

    public void setInviteRewardFirst(BigDecimal inviteRewardFirst) {
        this.inviteRewardFirst = inviteRewardFirst;
    }

    public BigDecimal getInvestRewardFirst() {
        return investRewardFirst;
    }

    public void setInvestRewardFirst(BigDecimal investRewardFirst) {
        this.investRewardFirst = investRewardFirst;
    }

    public Date getInvestTimeFirst() {
        return investTimeFirst;
    }

    public void setInvestTimeFirst(Date investTimeFirst) {
        this.investTimeFirst = investTimeFirst;
    }

    public Integer getInviteSecondUserId() {
        return inviteSecondUserId;
    }

    public void setInviteSecondUserId(Integer inviteSecondUserId) {
        this.inviteSecondUserId = inviteSecondUserId;
    }

    public Date getInviteSecondUserRegisterTime() {
        return inviteSecondUserRegisterTime;
    }

    public void setInviteSecondUserRegisterTime(Date inviteSecondUserRegisterTime) {
        this.inviteSecondUserRegisterTime = inviteSecondUserRegisterTime;
    }

    public BigDecimal getInviteRewardSecond() {
        return inviteRewardSecond;
    }

    public void setInviteRewardSecond(BigDecimal inviteRewardSecond) {
        this.inviteRewardSecond = inviteRewardSecond;
    }

    public BigDecimal getInvestRewardSecond() {
        return investRewardSecond;
    }

    public void setInvestRewardSecond(BigDecimal investRewardSecond) {
        this.investRewardSecond = investRewardSecond;
    }

    public Date getInvestTimeSecond() {
        return investTimeSecond;
    }

    public void setInvestTimeSecond(Date investTimeSecond) {
        this.investTimeSecond = investTimeSecond;
    }
}