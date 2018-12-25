package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum MovieEnum {

    ON_SHELF(0,"上架"),
    SHELF(1,"下架");

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

    MovieEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

}
