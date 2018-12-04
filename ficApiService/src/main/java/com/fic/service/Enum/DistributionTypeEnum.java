package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum DistributionTypeEnum {


    LEVEL_ONE(0,"直接"),
    LEVEL_TWO(1,"间接"),
    TYPE_REGISTER(0,"注册"),
    TYPE_INVEST(1,"投资");

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

    DistributionTypeEnum(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

}
