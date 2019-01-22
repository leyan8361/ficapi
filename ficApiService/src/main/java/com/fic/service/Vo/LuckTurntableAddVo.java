package com.fic.service.Vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class LuckTurntableAddVo {

    @ApiModelProperty(value = "奖品名称",required = true,example = "")
    private String priceName;

    @ApiModelProperty(value = "(100,封面)(0,谢谢参与)(1,影视金句)(2,微信号)(3,礼品)(4,电影票)(5,50TFC)(6,200TFC)(7,5000TFC)",required = true,example = "0")
    private Integer priceType;

    @ApiModelProperty(value = "概率",required = true,example = "1")
    private BigDecimal probability;

    @ApiModelProperty(value = "当price_type为币时，此项此须填",required = false)
    private BigDecimal amount;

    @ApiModelProperty(value = "顺序小的排前面,允许小数",example = "0",required = true)
    private BigDecimal sort;

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
        if(null == probability){
            return BigDecimal.ZERO;
        }
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

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }
}
