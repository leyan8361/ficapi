package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum  BingoStatusEnum {

    WAIT_BINGO(0,"待开奖"),
    BINGO(1,"已中奖"),
    UN_BINGO(2,"未中奖"),
    CLOSE_RETURNING(3,"关闭退回");

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

    BingoStatusEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }


}
