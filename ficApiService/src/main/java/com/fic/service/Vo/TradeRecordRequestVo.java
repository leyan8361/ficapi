package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TradeRecordRequestVo extends PageVo{

    @ApiModelProperty(value = "用户ID",required = true)
    private Integer userId;

    @ApiModelProperty(value = "查询月份",required = true,example = "2019-01")
    private String month;

    @ApiModelProperty(value = "类型(0,全部)(1,投资)(2,竞猜)(3,转账)(4,其它)",required = true)
    private Integer type;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
