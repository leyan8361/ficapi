package com.fic.service.entity;

import java.util.Date;

public class Movie {
    private Integer movieId;

    private String movieName;

    private String movieRemark;

    private Date createdTime;

    private Integer createdBy;

    private Date updatedTime;

    private Integer updatedBy;

    private String movieNote;

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

    public String getMovieNote() {
        return movieNote;
    }

    public void setMovieNote(String movieNote) {
        this.movieNote = movieNote;
    }
}