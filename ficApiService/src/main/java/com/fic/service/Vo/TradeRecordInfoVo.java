package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class TradeRecordInfoVo {

    @ApiModelProperty(value = "今日收益")
    private BigDecimal totalReceive;
    @ApiModelProperty(value = "今天支出")
    private BigDecimal totalPay;
    @ApiModelProperty(value = "交易记录item")
    private List<TradeRecordVo> records;

    public BigDecimal getTotalReceive() {
        return totalReceive;
    }

    public void setTotalReceive(BigDecimal totalReceive) {
        this.totalReceive = totalReceive;
    }

    public BigDecimal getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(BigDecimal totalPay) {
        this.totalPay = totalPay;
    }

    public List<TradeRecordVo> getRecords() {
        return records;
    }

    public void setRecords(List<TradeRecordVo> records) {
        this.records = records;
    }
}
