package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class InvestDetail {
    private Integer investDetailId;

    private Integer investId;

    private Integer userId;

    private Integer movieId;

    private BigDecimal amount;

    private Date obTime;

    private Date inTime;

    private String investDetailCode;

    private Date createdTime;

    private Integer createdBy;

    private Date updatedTime;

    private Integer updatedBy;

    public Integer getInvestDetailId() {
        return investDetailId;
    }

    public void setInvestDetailId(Integer investDetailId) {
        this.investDetailId = investDetailId;
    }

    public Integer getInvestId() {
        return investId;
    }

    public void setInvestId(Integer investId) {
        this.investId = investId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getObTime() {
        return obTime;
    }

    public void setObTime(Date obTime) {
        this.obTime = obTime;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getInvestDetailCode() {
        return investDetailCode;
    }

    public void setInvestDetailCode(String investDetailCode) {
        this.investDetailCode = investDetailCode;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}