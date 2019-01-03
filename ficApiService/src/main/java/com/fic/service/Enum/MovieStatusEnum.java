package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum MovieStatusEnum {

    COMPLETE(0,"已杀青"),
    WAIT_ON(1,"待开机"),
    DIVIDEND(2,"分红"),
    WAIT_DIVIDEND(3,"待分红");

    private Integer code;
    private String remark;

    MovieStatusEnum(Integer code, String remark) {
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
