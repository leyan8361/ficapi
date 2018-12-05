package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2018/11/28
 *   @Discription:
**/
@ApiModel
public class InvestBalanceInfoVo {

    @ApiModelProperty(value = "用户ID",example = "0")
    private Integer userId;
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public InvestBalanceInfoVo(Integer userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public InvestBalanceInfoVo() {
    }
}
