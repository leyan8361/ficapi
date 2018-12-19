package com.fic.service.Vo;

import java.math.BigDecimal;

public class BetWeekBoxCountVo {

    private BigDecimal betAmount;

    private BigDecimal weekBoxAmount;

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getWeekBoxAmount() {
        return weekBoxAmount;
    }

    public void setWeekBoxAmount(BigDecimal weekBoxAmount) {
        this.weekBoxAmount = weekBoxAmount;
    }
}
