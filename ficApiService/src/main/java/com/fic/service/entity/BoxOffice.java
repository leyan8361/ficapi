package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BoxOffice {
    private Integer id;

    private Integer movieId;

    private String movieName;

    private BigDecimal boxInfo;

    private String releaseDay;

    private BigDecimal sumBoxInfo;

    private Date sumDay;

    private String sumBoxInfoUnit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public BigDecimal getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(BigDecimal boxInfo) {
        this.boxInfo = boxInfo;
    }

    public String getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(String releaseDay) {
        this.releaseDay = releaseDay;
    }

    public BigDecimal getSumBoxInfo() {
        return sumBoxInfo;
    }

    public void setSumBoxInfo(BigDecimal sumBoxInfo) {
        this.sumBoxInfo = sumBoxInfo;
    }

    public Date getSumDay() {
        return sumDay;
    }

    public void setSumDay(Date sumDay) {
        this.sumDay = sumDay;
    }

    public String getSumBoxInfoUnit() {
        return sumBoxInfoUnit;
    }

    public void setSumBoxInfoUnit(String sumBoxInfoUnit) {
        this.sumBoxInfoUnit = sumBoxInfoUnit;
    }
}