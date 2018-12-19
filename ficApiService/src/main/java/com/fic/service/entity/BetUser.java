package com.fic.service.entity;

import java.math.BigDecimal;

public class BetUser {
    private Integer id;

    private Integer userId;

    private Integer betScenceMovieId;

    private String betWhich;

    private BigDecimal betAmount;

    private Byte bingo;

    private BigDecimal bingoPrice;

    private BigDecimal closeWithReturning;

    private BigDecimal tradeFee;

    private BigDecimal rewardPool;

    private BigDecimal betWeekTotalBox;

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

    public BigDecimal getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(BigDecimal tradeFee) {
        this.tradeFee = tradeFee;
    }

    public BigDecimal getRewardPool() {
        return rewardPool;
    }

    public void setRewardPool(BigDecimal rewardPool) {
        this.rewardPool = rewardPool;
    }

    public BigDecimal getBetAmount() {
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
}