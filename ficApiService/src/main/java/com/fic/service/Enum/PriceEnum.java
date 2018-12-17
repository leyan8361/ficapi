package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2018/12/17
 *   @Discription:
**/
public enum PriceEnum {

    ODD(0,"猜单双，单"),
    EVEN(1,"猜单双，双"),
    CAN(2,"猜票房能不能，能"),
    COULD_NOT(3,"猜票房能不能，不能"),
    A_CHOICE(4,"选择题A"),
    B_CHOICE(5,"选择题B"),
    C_CHOICE(6,"选择题C"),
    D_CHOICE(7,"选择题D");


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

    PriceEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }


}
