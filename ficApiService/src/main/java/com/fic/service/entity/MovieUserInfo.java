package com.fic.service.entity;

public class MovieUserInfo {
    private Integer id;

    private Byte likz;

    private Byte fav;

    private Integer userId;

    private Integer movieId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getLikz() {
        return likz;
    }

    public void setLikz(Byte likz) {
        this.likz = likz;
    }

    public Byte getFav() {
        return fav;
    }

    public void setFav(Byte fav) {
        this.fav = fav;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}