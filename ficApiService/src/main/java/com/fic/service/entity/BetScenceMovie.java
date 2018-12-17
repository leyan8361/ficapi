package com.fic.service.entity;

import java.util.Date;

public class BetScenceMovie {
    private Integer id;

    private Integer betMovieId;

    private Integer betScenceId;

    private Byte status;

    private Date startDay;

    private Date endDay;

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
}