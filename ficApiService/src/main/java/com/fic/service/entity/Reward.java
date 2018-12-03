package com.fic.service.entity;

import java.math.BigDecimal;

public class Reward {

    private Integer id;

    private Integer userLess;

    private Integer userGreater;

    private BigDecimal registerSelf;

    private BigDecimal inviteRewardFirst;

    private BigDecimal inviteRewardSecond;

    private BigDecimal investRewardFirst;

    private BigDecimal investRewardSecond;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserLess() {
        return userLess;
    }

    public void setUserLess(Integer userLess) {
        this.userLess = userLess;
    }

    public Integer getUserGreater() {
        return userGreater;
    }

    public void setUserGreater(Integer userGreater) {
        this.userGreater = userGreater;
    }

    public BigDecimal getRegisterSelf() {
        return registerSelf;
    }

    public void setRegisterSelf(BigDecimal registerSelf) {
        this.registerSelf = registerSelf;
    }

    public BigDecimal getInviteRewardFirst() {
        return inviteRewardFirst;
    }

    public void setInviteRewardFirst(BigDecimal inviteRewardFirst) {
        this.inviteRewardFirst = inviteRewardFirst;
    }

    public BigDecimal getInviteRewardSecond() {
        return inviteRewardSecond;
    }

    public void setInviteRewardSecond(BigDecimal inviteRewardSecond) {
        this.inviteRewardSecond = inviteRewardSecond;
    }

    public BigDecimal getInvestRewardFirst() {
        return investRewardFirst;
    }

    public void setInvestRewardFirst(BigDecimal investRewardFirst) {
        this.investRewardFirst = investRewardFirst;
    }

    public BigDecimal getInvestRewardSecond() {
        return investRewardSecond;
    }

    public void setInvestRewardSecond(BigDecimal investRewardSecond) {
        this.investRewardSecond = investRewardSecond;
    }
}