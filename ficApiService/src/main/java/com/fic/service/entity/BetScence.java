package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BetScence {
    private Integer id;

    private String betName;

    private Byte status;

    private Date createdTime;

    private Byte betType;

    private int hasJasckpot;

    private BigDecimal jasckpotFee;

    private BigDecimal totalJasckpot;

    private int hasReservation;

    private BigDecimal reservationFee;

    private BigDecimal totalReservation;

    public int getHasJasckpot() {
        return hasJasckpot;
    }

    public void setHasJasckpot(int hasJasckpot) {
        this.hasJasckpot = hasJasckpot;
    }

    public int getHasReservation() {
        return hasReservation;
    }

    public void setHasReservation(int hasReservation) {
        this.hasReservation = hasReservation;
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
        if(null == totalJasckpot){
            return BigDecimal.ZERO;
        }
        return totalJasckpot;
    }

    public void setTotalJasckpot(BigDecimal totalJasckpot) {
        this.totalJasckpot = totalJasckpot;
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
        if(null == totalReservation){
            return BigDecimal.ZERO;
        }
        return totalReservation;
    }

    public void setTotalReservation(BigDecimal totalReservation) {
        this.totalReservation = totalReservation;
    }

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

    @Override
    public String toString() {
        return "BetScence{" +
                "id=" + id +
                ", betName='" + betName + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", betType=" + betType +
                ", hasJasckpot=" + hasJasckpot +
                ", jasckpotFee=" + jasckpotFee +
                ", totalJasckpot=" + totalJasckpot +
                ", hasReservation=" + hasReservation +
                ", reservationFee=" + reservationFee +
                ", totalReservation=" + totalReservation +
                '}';
    }
}