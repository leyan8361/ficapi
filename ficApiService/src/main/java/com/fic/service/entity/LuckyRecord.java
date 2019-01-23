package com.fic.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class LuckyRecord {
    private Integer id;

    private Integer userId;

    @ApiModelProperty("中奖礼品ID")
    private Integer bingoPrice;

    @ApiModelProperty("(0，未中奖)(1,已中奖)")
    private Integer status;

    @ApiModelProperty("(0，未领取)(1,已领取)")
    private Integer isReceive;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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