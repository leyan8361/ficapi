package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetScenceVo {

//    @ApiModelProperty(value = "项目ID")
//    private Integer id;

    @ApiModelProperty(value = "项目名称",required = true)
    private String betName;

    @ApiModelProperty(value = "项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房)",required = true)
    private Byte betType;

    @ApiModelProperty(value = "(0，下架)(1,上架)，默认下架")
    private Byte status;


//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getBetName() {
        return betName;
    }

    public void setBetName(String betName) {
        this.betName = betName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getBetType() {
        return betType;
    }

    public void setBetType(Byte betType) {
        this.betType = betType;
    }

}
