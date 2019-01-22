package com.fic.service.Vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.math.BigInteger;


@ApiModel
public class TransactionRecordVo {

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("转入转出来源地址")
    private String fromAddress;

    @ApiModelProperty("转入转出目标地址")
    private String toAddress;

    @ApiModelProperty("转账金额")
    private BigDecimal amount;

    @ApiModelProperty("Gas单价")
    private BigDecimal gasPrice;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("合约地址")
    private String transactionAddress;

    @ApiModelProperty("Gas最大数量")
    private BigDecimal gasLimit;

    @ApiModelProperty("状态(0,转账申请)(1,转账申请被拒绝)(2,转账待确认,)(3,转账成功)(4,转账失败)")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("(0,转入)(1,转出)")
    private Integer way;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
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

    public BigDecimal getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigDecimal gasLimit) {
        this.gasLimit = gasLimit;
    }
}
