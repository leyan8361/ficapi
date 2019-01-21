package com.fic.service.Vo;

import com.fic.service.entity.TransactionRecord;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public class OmTransactionRecordVo extends TransactionRecord {

    private String telephone;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
