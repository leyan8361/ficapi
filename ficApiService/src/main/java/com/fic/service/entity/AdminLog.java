package com.fic.service.entity;

import com.fic.service.utils.DateUtil;

import java.util.Date;

public class AdminLog {
    private Integer adminId;

    private String event;

    private Date operationTime;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public AdminLog(String event, Date operationTime) {
        this.event = event;
        this.operationTime = operationTime;
    }

    @Override
    public String toString() {
        return "AdminLog{" +
                "adminId=" + adminId +
                ", event='" + event + '\'' +
                ", operationTime=" + DateUtil.formatSec(operationTime) +
                '}';
    }
}