package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2018/12/19
 *   @Discription: 竞猜状态
**/
public enum BetScenceMovieStatusEnum {

    WAIT(0,"待开奖"),
    DRAW(1,"已开奖"),
    CLOSE_RETURN(2,"已退还关闭"),
    CLOSE_NO_BET(3,"关闭无人投注");

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

    BetScenceMovieStatusEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

}
