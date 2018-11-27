package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:
**/
@ApiModel
public class ExchangeRateVo {

    @ApiModelProperty(value = "币1，换算单位")
    private String coin1;
    @ApiModelProperty(value = "币2，被换算单位")
    private String coin2;
    @ApiModelProperty(value = "换算率")
    private BigDecimal rate;

    public String getCoin1() {
        return coin1;
    }

    public void setCoin1(String coin1) {
        this.coin1 = coin1;
    }

    public String getCoin2() {
        return coin2;
    }

    public void setCoin2(String coin2) {
        this.coin2 = coin2;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
