package com.fic.service.entity;

import java.util.Date;

public class BannerInfo {
    private Integer id;

    private String bannerUrl;

    private Integer inOrder;

    private String jumpUrlAndroid;

    private String jumpUrlIos;

    private Date createdTime;

    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Integer getInOrder() {
        return inOrder;
    }

    public void setInOrder(Integer inOrder) {
        this.inOrder = inOrder;
    }

    public String getJumpUrlAndroid() {
        return jumpUrlAndroid;
    }

    public void setJumpUrlAndroid(String jumpUrlAndroid) {
        this.jumpUrlAndroid = jumpUrlAndroid;
    }

    public String getJumpUrlIos() {
        return jumpUrlIos;
    }

    public void setJumpUrlIos(String jumpUrlIos) {
        this.jumpUrlIos = jumpUrlIos;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BannerInfo{" +
                "id=" + id +
                ", bannerUrl='" + bannerUrl + '\'' +
                ", inOrder=" + inOrder +
                ", jumpUrlAndroid='" + jumpUrlAndroid + '\'' +
                ", jumpUrlIos='" + jumpUrlIos + '\'' +
                ", createdTime=" + createdTime +
                ", status=" + status +
                '}';
    }
}