package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2018/12/3
 *   @Discription:
**/
public enum DistributionStatusEnum {

    NO_DISTRBUTE(0,"无分销"),
    FIRST_LEVEL(1,"1级"),
    SECOND_LEVEL(2,"2级");

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
