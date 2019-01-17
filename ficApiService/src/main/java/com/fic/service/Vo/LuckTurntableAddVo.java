package com.fic.service.Vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class LuckTurntableAddVo {

    @ApiModelProperty(value = "奖品名称",required = true,example = "100TFC")
    private String priceName;

    @ApiModelProperty(value = "奖品类型(0,币)(1,实物)",required = true,example = "0")
    private Integer priceType;

    @ApiModelProperty(value = "概率(注：所有奖品概率加起来应为100%)",required = true,example = "12.5")
    private BigDecimal probability;

    @ApiModelProperty(value = "当price_type为币(0)时，此项此须填",required = true,example = "100")
    private BigDecimal amount;

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
