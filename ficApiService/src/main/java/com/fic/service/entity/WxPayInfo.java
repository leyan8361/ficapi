package com.fic.service.entity;

import java.util.Date;

public class WxPayInfo {
    private Integer id;

    private Integer userId;

    private String prepayId;

    private String orderNum;

    private Integer status;

    private Integer payStatus;

    private Integer refundStatus;

    private String requestBody;

    private String responseBody;

    private String sign;

    private String noncestr;

    private String transactionId;

    private String refundId;

    private String amount;

    private String refundNo;

    private Date notifyTime;

    private Date createdTime;

    private Date updatedTime;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WxPayInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", prepayId='" + prepayId + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", status=" + status +
                ", payStatus=" + payStatus +
                ", refundStatus=" + refundStatus +
                ", requestBody='" + requestBody + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", sign='" + sign + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", refundId='" + refundId + '\'' +
                ", amount='" + amount + '\'' +
                ", refundNo='" + refundNo + '\'' +
                ", notifyTime=" + notifyTime +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", type=" + type +
                '}';
    }
}