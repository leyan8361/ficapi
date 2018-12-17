package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2018/12/4
 *   @Discription: ShelfStatusEnum type
 **/
public enum ShelfStatusEnum {

    SHELF(0,"下架"),
    ON_SHELF(1,"上架")
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

    ShelfStatusEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
