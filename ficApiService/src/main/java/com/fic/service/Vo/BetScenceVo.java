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
public class BetScenceVo {

    @ApiModelProperty(value = "项目ID")
    private Integer id;

    @ApiModelProperty(value = "项目名称",required = true)
    private String betName;

    @ApiModelProperty(value = "项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房)",required = true)
    private Byte betType;

    @ApiModelProperty(value = "手续费 %比",required = true)
    private BigDecimal betFee;

    @ApiModelProperty(value = "中奖类型，待定")
    private Integer bingoType;

    @ApiModelProperty(value = "开奖时间",required = true)
    private Date bingoTime;

    @ApiModelProperty(value = "(0，下架)(1,上架)，默认下架")
    private Byte status;

    @ApiModelProperty(value = "是否有奖池(0,无奖励)(1,有奖池)",required = true)
    private Byte hasJasckpot;

    @ApiModelProperty(value = "奖池(竞猜手续费抽取)，投注百分比",example = "2")
    private BigDecimal jasckpotFee;

    @ApiModelProperty(value = "是否有备用金(0,无)(1,有)",required = true)
    private Byte hasReservation;

    @ApiModelProperty(value = "备用金(竞猜手续费抽取)，投注百分比",example = "2")
    private BigDecimal reservationFee;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBetName() {
        return betName;
    }

    public void setBetName(String betName) {
        this.betName = betName;
    }

    public BigDecimal getBetFee() {
        return betFee;
    }

    public void setBetFee(BigDecimal betFee) {
        this.betFee = betFee;
    }

    public Integer getBingoType() {
        return bingoType;
    }

    public void setBingoType(Integer bingoType) {
        this.bingoType = bingoType;
    }

    public Date getBingoTime() {
        return bingoTime;
    }

    public void setBingoTime(Date bingoTime) {
        this.bingoTime = bingoTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getBetType() {
        return betType;
    }

    public void setBetType(Byte betType) {
        this.betType = betType;
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
}
