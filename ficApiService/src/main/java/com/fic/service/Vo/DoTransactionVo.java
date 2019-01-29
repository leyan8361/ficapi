package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2019/1/9
 *   @Discription:
**/
@ApiModel
public class DoTransactionVo {

    @ApiModelProperty(value = "用户ID",required = true)
    private Integer userId;

    @ApiModelProperty(value = "转出目标地址",required = true)
    private String toAddress;

    @ApiModelProperty(value = "转账金额(TFC)",required = true)
    private BigDecimal amount;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

}
