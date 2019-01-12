package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceStatement {
    private Integer id;

    private Integer userId;

    private BigDecimal amount;

    private Integer type;

    private Integer way;

    private Date createdTime;

    private Integer distributionId;

    private Integer investDetailId;

    private BigDecimal balance;

    private Integer traceId;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(Integer distributionId) {
        this.distributionId = distributionId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getInvestDetailId() {
        return investDetailId;
    }

    public void setInvestDetailId(Integer investDetailId) {
        this.investDetailId = investDetailId;
    }

    public Integer getTraceId() {
        return traceId;
    }

    public void setTraceId(Integer traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "BalanceStatement{" +
                "id=" + id +
                ", userId=" + userId +
                ", amount=" + amount +
                ", type=" + type +
                ", way=" + way +
                ", createdTime=" + createdTime +
                ", distributionId=" + distributionId +
                ", investDetailId=" + investDetailId +
                ", balance=" + balance +
                ", traceId=" + traceId +
                '}';
    }
}