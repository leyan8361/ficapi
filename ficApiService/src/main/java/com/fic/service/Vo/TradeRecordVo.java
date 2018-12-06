package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class TradeRecordVo {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
    @ApiModelProperty(value = "手机号")
    private String telephone;
    @ApiModelProperty(value = "类型, 0佣金，1投资，2收益，3提现")
    private Integer type;
    @ApiModelProperty(value = "电影名")
    private String moveName;
    @ApiModelProperty(value = "0收入，1支出")
    private Integer way;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "创建时间")
    private String createdTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
