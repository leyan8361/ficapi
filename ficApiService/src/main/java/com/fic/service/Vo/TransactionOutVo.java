package com.fic.service.Vo;

import com.fic.service.Enum.ErrorCodeEnum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public class TransactionOutVo {

    private boolean success;

    private String txHash;

    private ErrorCodeEnum errorCodeEnum;

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
}
