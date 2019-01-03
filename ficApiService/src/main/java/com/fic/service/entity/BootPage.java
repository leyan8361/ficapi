package com.fic.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class BootPage {
    private Integer id;

    private String bootPageUrl;

    private Integer sort;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private Integer isShow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBootPageUrl() {
        return bootPageUrl;
    }

    public void setBootPageUrl(String bootPageUrl) {
        this.bootPageUrl = bootPageUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}