package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2018/12/4
 *   @Discription:
**/
public enum FinanceWayEnum {

    IN(0,"入账"),
    OUT(1,"出账");

    private Integer code;
    private String remark;

    FinanceWayEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

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
}
