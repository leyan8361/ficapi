package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BetScence {
    private Integer id;

    private String betName;

    private BigDecimal betFee;

    private Integer bingoType;

    private Date bingoTime;

    private Byte status;

    private Date createdTime;

    private Byte betType;

    private Byte hasJasckpot;

    private BigDecimal jasckpotFee;

    private BigDecimal totalJasckpot;

    private Byte hasReservation;

    private BigDecimal reservationFee;

    private BigDecimal totalReservation;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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