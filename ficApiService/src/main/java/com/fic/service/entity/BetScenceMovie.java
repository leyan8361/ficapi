package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BetScenceMovie {
    private Integer id;

    private Integer betMovieId;

    private Integer betScenceId;

    private Byte status;

    private Date startDay;

    private Date endDay;

    private String drawResult;


    private BigDecimal totalReservationReturning;

    private String guessOverUnit;

    private String choiceInput;

    private String sumBoxInput;

    private BigDecimal bingoOdds;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public String getGuessOverUnit() {
        return guessOverUnit;
    }

    public void setGuessOverUnit(String guessOverUnit) {
        this.guessOverUnit = guessOverUnit;
    }

    public String getChoiceInput() {
        return choiceInput;
    }

    public void setChoiceInput(String choiceInput) {
        this.choiceInput = choiceInput;
    }

    public String getSumBoxInput() {
        return sumBoxInput;
    }

    public void setSumBoxInput(String sumBoxInput) {
        this.sumBoxInput = sumBoxInput;
    }

    public BigDecimal getBingoOdds() {
        if(null == bingoOdds){
            return BigDecimal.ZERO.setScale(0,BigDecimal.ROUND_DOWN);
        }
        return bingoOdds;
    }

    public void setBingoOdds(BigDecimal bingoOdds) {
        this.bingoOdds = bingoOdds;
    }

    public BigDecimal getTotalReservationReturning() {
        if(null == totalReservationReturning){
            return BigDecimal.ZERO;
        }
        return totalReservationReturning;
    }

    public void setTotalReservationReturning(BigDecimal totalReservationReturning) {
        this.totalReservationReturning = totalReservationReturning;
    }

    @Override
    public String toString() {
        return "BetScenceMovie{" +
                "id=" + id +
                ", betMovieId=" + betMovieId +
                ", betScenceId=" + betScenceId +
                ", status=" + status +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", drawResult='" + drawResult + '\'' +
                ", totalReservationReturning=" + totalReservationReturning +
                ", guessOverUnit='" + guessOverUnit + '\'' +
                ", choiceInput='" + choiceInput + '\'' +
                ", sumBoxInput='" + sumBoxInput + '\'' +
                ", bingoOdds=" + bingoOdds +
                '}';
    }
}