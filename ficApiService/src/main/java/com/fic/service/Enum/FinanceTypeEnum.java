package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2018/12/4
 *   @Discription: balance_statement type
**/
public enum FinanceTypeEnum {

    RECHARGE(0,"充值"),
    INCOME(1,"收益"),
    DIVIDENDS(2,"分红"),
    WITHDRAW(3,"提现"),
    INVEST(4,"投资"),
    REWARD(5,"奖励"),
    BET_REWARD(6,"竞猜奖励"),
    BET_RETURNING(7,"竞猜返还"),
    BET(8,"投注"),
    BET_REWARD_POOL(9,"连续奖励"),
    TRANSFER_OUT(10,"转出成功"),
    PAYEE_IN(11,"转入成功"),
    TRANSFERING(12,"转账中")
    DRAW(13,"抽奖"),
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

    FinanceTypeEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
