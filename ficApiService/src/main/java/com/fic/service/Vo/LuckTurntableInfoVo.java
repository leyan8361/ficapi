package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

@ApiModel
public class LuckTurntableInfoVo {

    @ApiModelProperty("转盘封面")
    private String coverUrl;

    @ApiModelProperty("用户余额")
    private BigDecimal balance;

    @ApiModelProperty("奖品项")
    private List<LuckTurntablePriceVo> priceList;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<LuckTurntablePriceVo> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<LuckTurntablePriceVo> priceList) {
        this.priceList = priceList;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
