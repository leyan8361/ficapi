package com.fic.service.Vo;

import com.fic.service.Enum.ErrorCodeEnum;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public class TransactionOutVo {

    private boolean success;

    private String txHash;

    private ErrorCodeEnum errorCodeEnum;

    private BigInteger gasPrice;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public ErrorCodeEnum getErrorCodeEnum() {
        return errorCodeEnum;
    }

    public void setErrorCodeEnum(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }
}
