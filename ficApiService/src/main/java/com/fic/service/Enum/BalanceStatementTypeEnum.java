package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2018/12/4
 *   @Discription: balance_statement type
**/
public enum BalanceStatementTypeEnum {

    RECHARGE(0,"充值"),
    INCOME(1,"收益"),
    DIVIDENDS(2,"分红"),
    WITHDRAW(3,"提现"),
    INVEST(4,"投资"),
    REWARD(5,"奖励")
    ;

    private Integer code;
    private String remark;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    BalanceStatementTypeEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
