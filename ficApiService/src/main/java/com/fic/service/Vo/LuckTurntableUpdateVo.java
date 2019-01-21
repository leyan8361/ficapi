package com.fic.service.Vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class LuckTurntableUpdateVo {

    @ApiModelProperty(value = "奖品ID",required = true)
    private int id;

    @ApiModelProperty(value = "奖品名称",required = false)
    private String priceName;

    @ApiModelProperty(value = "(100,封面)(0,谢谢参与)(1,影视金句)(2,微信号)(3,礼品)(4,电影票)(5,50TFC)(6,200TFC)(7,5000TFC)",required = true,example = "0")
    private Integer priceType;

    @ApiModelProperty(value = "概率(注：所有奖品概率加起来应为100%)",required = false)
    private BigDecimal probability;

    @ApiModelProperty(value = "当price_type为币(0)时，此项此须填",required = false)
    private BigDecimal amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
