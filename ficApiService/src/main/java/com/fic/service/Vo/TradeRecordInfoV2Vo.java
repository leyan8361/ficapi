package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 交易明细Vo
 */
@ApiModel
public class TradeRecordInfoV2Vo {

    @ApiModelProperty(value = "本月收入")
    private BigDecimal income;

    @ApiModelProperty(value = "本月支出")
    private BigDecimal expend;

    @ApiModelProperty(value = "明细列表")
    private List<TradeRecordV2Vo> items;

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpend() {
        return expend;
    }

    public void setExpend(BigDecimal expend) {
        this.expend = expend;
    }

    public List<TradeRecordV2Vo> getItems() {
        return items;
    }

    public void setItems(List<TradeRecordV2Vo> items) {
        this.items = items;
    }
}
