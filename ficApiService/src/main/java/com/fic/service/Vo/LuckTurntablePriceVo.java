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

    @ApiModelProperty(value = "奖品类型")
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
}
