package com.fic.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
public class ExchangeRate {

    @ApiModelProperty(value = "id")
    private int id;

    @ApiModelProperty(value = "换算结果单位(BTC,BCH,ETH,TFC)",example = "TFC")
    private String coin1;

    @ApiModelProperty(value = "被换算币种(BTC,BCH,ETH,TFC)",example = "BTC")
    private String coin2;

    @ApiModelProperty("coin2 / coin1")
    private BigDecimal rate;

//    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private Integer createdBy;

    private Date updatedTime;

    private Integer updatedBy;

    public String getCoin1() {
        return coin1;
    }

    public void setCoin1(String coin1) {
        this.coin1 = coin1;
    }

    public String getCoin2() {
        return coin2;
    }

    public void setCoin2(String coin2) {
        this.coin2 = coin2;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}