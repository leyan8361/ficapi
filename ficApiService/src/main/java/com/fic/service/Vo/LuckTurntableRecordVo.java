package com.fic.service.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class LuckTurntableRecordVo {

    @ApiModelProperty("抽奖记录ID")
    private Integer recordId;

    @ApiModelProperty("奖品内容")
    private String priceName;

    @ApiModelProperty("奖品类型(0,谢谢参与)(1,影视金句)(2,微信号)(3,礼品)(4,电影票)(5,50TFC)(6,200TFC)(7,5000TFC)")
    private int priceType;

    @ApiModelProperty("中奖时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date bingoTime;

    @ApiModelProperty("领取状态(0，未领取)(1,已兑换)")
    private int isReceive;

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public Date getBingoTime() {
        return bingoTime;
    }

    public void setBingoTime(Date bingoTime) {
        this.bingoTime = bingoTime;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }
}
