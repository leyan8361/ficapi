package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum TradeRecordDistributionEnum {

    REGISTER(0,"注册奖励"),
    INVITE_ONE(1,"邀请好友注册奖励"),
    INVITE_TWO(2,"好友邀请新用户注册奖励"),
    INVEST_ONE(3,"邀请好友投资奖励"),
    INVEST_TWO(4,"好友邀请新用户投资奖励");

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

    TradeRecordDistributionEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

}
