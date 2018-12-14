package com.fic.service.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Movie {
    private Integer movieId;

    private String movieName;

    private String movieRemark;

    private Date createdTime;

    private Integer createdBy;

    private Date updatedTime;

    private Integer updatedBy;

    private String movieType;

    private BigDecimal budget;

    private BigDecimal quota;

    private String movieCoverUrl;

    private String showPlace;

    private Date showTime;

    private String movieNote;

    private byte status;

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

    public String getMovieRemark() {
        return movieRemark;
    }

    public void setMovieRemark(String movieRemark) {
        this.movieRemark = movieRemark;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public String getMovieCoverUrl() {
        return movieCoverUrl;
    }

    public void setMovieCoverUrl(String movieCoverUrl) {
        this.movieCoverUrl = movieCoverUrl;
    }

    public String getShowPlace() {
        return showPlace;
    }

    public void setShowPlace(String showPlace) {
        this.showPlace = showPlace;
    }

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    public String getMovieNote() {
        return movieNote;
    }

    public void setMovieNote(String movieNote) {
        this.movieNote = movieNote;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", movieRemark='" + movieRemark + '\'' +
                ", createdTime=" + createdTime +
                ", createdBy=" + createdBy +
                ", updatedTime=" + updatedTime +
                ", updatedBy=" + updatedBy +
                ", movieType='" + movieType + '\'' +
                ", budget=" + budget +
                ", quota=" + quota +
                ", movieCoverUrl='" + movieCoverUrl + '\'' +
                ", showPlace='" + showPlace + '\'' +
                ", showTime=" + showTime +
                ", movieNote='" + movieNote + '\'' +
                ", status=" + status +
                '}';
    }
}