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

    @ApiModelProperty("转入转出来源地址,当转出时，此项不用填写")
    private String fromAddress;

    @ApiModelProperty("转入转出目标地址,当转入时，此项不用填写")
    private String toAddress;

    @ApiModelProperty(value = "转账金额",required = true)
    private BigDecimal amount;

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

}
