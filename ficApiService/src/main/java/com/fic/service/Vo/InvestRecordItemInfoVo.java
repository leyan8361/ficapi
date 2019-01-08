package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class InvestRecordItemInfoVo {

    @ApiModelProperty(value = "投资记录ID",example = "0")
    private Integer investDetailId;
    @ApiModelProperty(value = "电影名字")
    private String movieName;
    @ApiModelProperty(value = "投资金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)")
    private int status;
    @ApiModelProperty(value = "投资时间")
    private String inTime;
    @ApiModelProperty(value = "流水号")
    private String code;

    public Integer getInvestDetailId() {
        return investDetailId;
    }

    public void setInvestDetailId(Integer investDetailId) {
        this.investDetailId = investDetailId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
