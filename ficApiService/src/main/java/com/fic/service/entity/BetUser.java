package com.fic.service.entity;

import java.math.BigDecimal;

public class BetUser {
    private Integer id;

    private Integer userId;

    private Integer betScenceMovieId;

    private String betWhich;

    private Integer bingo;

    private BigDecimal bingoPrice;

    private BigDecimal closeWithReturning;

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

    public Integer getBingo() {
        return bingo;
    }

    public void setBingo(Integer bingo) {
        this.bingo = bingo;
    }
}