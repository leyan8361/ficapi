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

    @ApiModelProperty(value = "(0，下架)(1,上架)，默认下架")
    private Byte status;

    @ApiModelProperty(value = "(0，无)(1,有)")
    private int hasJasckpot;

    @ApiModelProperty(value = "手续费%")
    private BigDecimal jasckpotFee;

    @ApiModelProperty(value = "累计奖池")
    private BigDecimal totalJasckpot;

    @ApiModelProperty(value = "(0，无)(1,有)")
    private int hasReservation;

    @ApiModelProperty(value = "备用金抽取竞彩金额%")
    private BigDecimal reservationFee;

    @ApiModelProperty(value = "累计备用金")
    private BigDecimal totalReservation;

    public int getHasJasckpot() {
        return hasJasckpot;
    }

    public void setHasJasckpot(int hasJasckpot) {
        this.hasJasckpot = hasJasckpot;
    }

    public BigDecimal getJasckpotFee() {
        return jasckpotFee;
    }

    public void setJasckpotFee(BigDecimal jasckpotFee) {
        this.jasckpotFee = jasckpotFee;
    }

    public int getHasReservation() {
        return hasReservation;
    }

    public void setHasReservation(int hasReservation) {
        this.hasReservation = hasReservation;
    }

    public BigDecimal getReservationFee() {
        return reservationFee;
    }

    public void setReservationFee(BigDecimal reservationFee) {
        this.reservationFee = reservationFee;
    }

    public String getBetName() {
        return betName;
    }

    public void setBetName(String betName) {
        this.betName = betName;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotalJasckpot() {
        return totalJasckpot;
    }

    public void setTotalJasckpot(BigDecimal totalJasckpot) {
        this.totalJasckpot = totalJasckpot;
    }

    public BigDecimal getTotalReservation() {
        return totalReservation;
    }

    public void setTotalReservation(BigDecimal totalReservation) {
        this.totalReservation = totalReservation;
    }
}
