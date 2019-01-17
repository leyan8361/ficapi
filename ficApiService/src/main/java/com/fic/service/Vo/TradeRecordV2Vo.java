package com.fic.service.Vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易明细item
 */
@ApiModel
public class TradeRecordV2Vo {

    @ApiModelProperty(value = "(0，充值)(1,收益)(2,分红)(3，提现)(4，投资)(5,奖励[分销(注册，投资)])(6,竞猜奖励)(7,竞猜返还)(8，投注)(9,连续奖励)(10,转出成功)(11,转入成功)(12,抽奖)")
    private Integer type;
    @ApiModelProperty(value = "电影名")
    private String moveName;
    @ApiModelProperty(value = "0收入，1支出")
    private Integer way;
    @ApiModelProperty(value = "分销标记, 当type 为5时，(0,注册奖励)(1,邀请好友注册奖励)(2,好友邀请新用户注册奖励)(3,邀请好友投资奖励)(4,好友邀请新用户投资奖励)；当type 不是5时，为null")
    private Integer distributionType;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public Integer getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(Integer distributionType) {
        this.distributionType = distributionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
