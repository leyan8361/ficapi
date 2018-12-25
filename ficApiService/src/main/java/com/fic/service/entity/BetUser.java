package com.fic.service.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class BetUser {
    private Integer id;

    private Integer userId;

    private Integer betScenceMovieId;

    private String betWhich;

    private BigDecimal betAmount;

    private Byte bingo;

    private BigDecimal bingoPrice;

    private BigDecimal closeWithReturning;

    private BigDecimal betFee;

    private BigDecimal reserveFee;

    private BigDecimal betWeekTotalBox;

    private Date createdTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBetScenceMovieId() {
        return betScenceMovieId;
    }

    public void setBetScenceMovieId(Integer betScenceMovieId) {
        this.betScenceMovieId = betScenceMovieId;
    }

    public String getBetWhich() {
        return betWhich;
    }

    public void setBetWhich(String betWhich) {
        this.betWhich = betWhich;
    }

    public Byte getBingo() {
        return bingo;
    }

    public void setBingo(Byte bingo) {
        this.bingo = bingo;
    }

    public BigDecimal getBingoPrice() {
        if(null == bingoPrice){
            return BigDecimal.ZERO.setScale(0,BigDecimal.ROUND_DOWN);
        }
        return bingoPrice;
    }

    public void setBingoPrice(BigDecimal bingoPrice) {
        this.bingoPrice = bingoPrice;
    }

    public BigDecimal getCloseWithReturning() {
        return closeWithReturning;
    }

    public void setCloseWithReturning(BigDecimal closeWithReturning) {
        this.closeWithReturning = closeWithReturning;
    }

    public BigDecimal getBetFee() {
        if(null == this.betFee){
            return BigDecimal.ZERO;
        }
        return betFee;
    }

    public void setBetFee(BigDecimal betFee) {
        this.betFee = betFee;
    }

    public BigDecimal getReserveFee() {
        if(null == this.reserveFee){
            return BigDecimal.ZERO;
        }
        return reserveFee;
    }

    public void setReserveFee(BigDecimal reserveFee) {
        this.reserveFee = reserveFee;
    }

    public BigDecimal getBetAmount() {
        if(null == betAmount){
            return BigDecimal.ZERO.setScale(0, RoundingMode.DOWN);
        }
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getBetWeekTotalBox() {
        return betWeekTotalBox;
    }

    public void setBetWeekTotalBox(BigDecimal betWeekTotalBox) {
        this.betWeekTotalBox = betWeekTotalBox;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}