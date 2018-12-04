package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2018/12/3
 *   @Discription:
**/
public enum DistributionStatusEnum {

    /**
     * 注册
     */
    REGISTER_NO_DISTRBUTE(0,"无分销"),
    REGISTER_FIRST_LEVEL(1,"1级"),
    REGISTER_SECOND_LEVEL(2,"2级"),
    /**
     * 投资
     */
    INVEST_NO_DISTRBUTE(0,"无分销"),
    INVEST_FIRST_LEVEL(1,"1级分销"),
    INVEST_SECOND_LEVEL(2,"2级分销");

    private int code;
    private String remark;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    DistributionStatusEnum(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
