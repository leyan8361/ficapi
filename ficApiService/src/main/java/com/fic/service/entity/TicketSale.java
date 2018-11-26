package com.fic.service.entity;

import java.util.Date;

public class TicketSale {
    private Integer ticketSaleId;

    private Integer ticketId;

    private Integer userId;

    private Date createdTime;

    private Integer createdBy;

    private Date updatedTime;

    private Integer updatedBy;

    public Integer getTicketSaleId() {
        return ticketSaleId;
    }

    public void setTicketSaleId(Integer ticketSaleId) {
        this.ticketSaleId = ticketSaleId;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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