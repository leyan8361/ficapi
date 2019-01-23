package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class LuckTurntablePriceVo {

    @ApiModelProperty(value = "奖品ID")
    private int priceId;

    @ApiModelProperty(value = "奖品名称")
    private String priceName;

    @ApiModelProperty(value = "奖品类型(0,10TFC)(1,影视金句)(2,微信号)(3,礼品)(4,电影票)(5,50TFC)(6,200TFC)(7,5000TFC)")
    private Integer priceType;

    @ApiModelProperty(value = "奖品中奖概率")
    private BigDecimal probability;

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }
}
