package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetScenceMovieResponseVo {

    @ApiModelProperty(value = "场次ID")
    private Integer id;

    @ApiModelProperty(value = "竞猜电影ID")
    private Integer betMovieId;

    @ApiModelProperty(value = "竞猜项目ID")
    private Integer betScenceId;

    @ApiModelProperty(value = "开始时间")
    private Date startDay;

    @ApiModelProperty(value = "结束时间")
    private Date endDay;

    @ApiModelProperty(value = "开奖结果 见PriceEnum")
    private String drawResult;

    @ApiModelProperty(value = "是否有奖池(0，无)(1，有)",required = true)
    private Byte hasJasckpot;

    @ApiModelProperty( value = "竞猜抽取手续费-->奖池(%)")
    private BigDecimal jasckpotFee;

    @ApiModelProperty(value = "奖池累计")
    private BigDecimal totalJasckpot;

    @ApiModelProperty(value = "是否有备用金(0，无)(1，有)",required = true)
    private Byte hasReservation;

    @ApiModelProperty(value = "竞猜抽取手续费-->备用金(%)")
    private BigDecimal reservationFee;

    @ApiModelProperty(value = "备用金累计")
    private BigDecimal totalReservation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBetMovieId() {
        return betMovieId;
    }

    public void setBetMovieId(Integer betMovieId) {
        this.betMovieId = betMovieId;
    }

    public Integer getBetScenceId() {
        return betScenceId;
    }

    public void setBetScenceId(Integer betScenceId) {
        this.betScenceId = betScenceId;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public String getDrawResult() {
        return drawResult;
    }

    public void setDrawResult(String drawResult) {
        this.drawResult = drawResult;
    }

    public Byte getHasJasckpot() {
        return hasJasckpot;
    }

    public void setHasJasckpot(Byte hasJasckpot) {
        this.hasJasckpot = hasJasckpot;
    }

    public BigDecimal getJasckpotFee() {
        return jasckpotFee;
    }

    public void setJasckpotFee(BigDecimal jasckpotFee) {
        this.jasckpotFee = jasckpotFee;
    }

    public BigDecimal getTotalJasckpot() {
        return totalJasckpot;
    }

    public void setTotalJasckpot(BigDecimal totalJasckpot) {
        this.totalJasckpot = totalJasckpot;
    }

    public Byte getHasReservation() {
        return hasReservation;
    }

    public void setHasReservation(Byte hasReservation) {
        this.hasReservation = hasReservation;
    }

    public BigDecimal getReservationFee() {
        return reservationFee;
    }

    public void setReservationFee(BigDecimal reservationFee) {
        this.reservationFee = reservationFee;
    }

    public BigDecimal getTotalReservation() {
        return totalReservation;
    }

    public void setTotalReservation(BigDecimal totalReservation) {
        this.totalReservation = totalReservation;
    }
}
