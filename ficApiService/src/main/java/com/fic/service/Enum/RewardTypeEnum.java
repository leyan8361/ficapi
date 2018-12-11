package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum RewardTypeEnum {

    OM_REWARD(0,"运营奖励"),
    DISTRUBTION_REWARD(1,"分销奖励"),
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

    RewardTypeEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

}
