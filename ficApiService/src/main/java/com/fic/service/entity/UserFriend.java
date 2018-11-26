package com.fic.service.entity;

import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription:
**/
public class UserFriend {
    private Integer userFriendId;

    private Integer userId;

    private Integer fUserId;

    private Date createdTime;

    private Integer createdBy;

    private Date updatedTime;

    private Integer updatedBy;

    public Integer getUserFriendId() {
        return userFriendId;
    }

    public void setUserFriendId(Integer userFriendId) {
        this.userFriendId = userFriendId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getfUserId() {
        return fUserId;
    }

    public void setfUserId(Integer fUserId) {
        this.fUserId = fUserId;
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
}