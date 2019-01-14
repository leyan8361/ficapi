package com.fic.service.Vo;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public class QueryTransactionResultVo {

    private String transactionHash;
    private BigDecimal gasUsed;
    private int status;
    private String from;
    private String to;
    private boolean success;

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public BigDecimal getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigDecimal gasUsed) {
        this.gasUsed = gasUsed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
