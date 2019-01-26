package com.fic.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TransactionRecord {

    private Integer id;

    private Integer userId;

    private String fromAddress;

    private String toAddress;

    private BigDecimal amount;

    private BigDecimal fee;

    private String transactionAddress;

    private Integer status;

    private String remark;

    private Integer way;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String transactionHash;

    private String coinType;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inComeTime;

    private BigDecimal coinRate;

    private BigDecimal coinNum;

    private BigDecimal gasPrice;

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

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getTransactionAddress() {
        return transactionAddress;
    }

    public void setTransactionAddress(String transactionAddress) {
        this.transactionAddress = transactionAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public Date getInComeTime() {
        return inComeTime;
    }

    public void setInComeTime(Date inComeTime) {
        this.inComeTime = inComeTime;
    }

    public BigDecimal getCoinRate() {
        return coinRate;
    }

    public void setCoinRate(BigDecimal coinRate) {
        this.coinRate = coinRate;
    }

    public BigDecimal getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(BigDecimal coinNum) {
        this.coinNum = coinNum;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", transactionAddress='" + transactionAddress + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", way=" + way +
                ", createdTime=" + createdTime +
                ", transactionHash='" + transactionHash + '\'' +
                ", coinType='" + coinType + '\'' +
                ", inComeTime=" + inComeTime +
                ", coinRate=" + coinRate +
                ", coinNum=" + coinNum +
                ", gasPrice=" + gasPrice +
                '}';
    }
}