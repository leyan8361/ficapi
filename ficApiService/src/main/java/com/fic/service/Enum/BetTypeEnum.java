package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum BetTypeEnum {

    ODD_EVEN(0,"猜单双"),
    OVER_BOX_OFFICE(1,"是否能超过竞猜票房"),
    CHOICE(2,"选择题"),
    TOTAL_BOX_OFFICE(3,"竞猜总票房");


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

    BetTypeEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

}
