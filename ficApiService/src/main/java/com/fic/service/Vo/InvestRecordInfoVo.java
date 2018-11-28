package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author Xie
 * @Date 2018/11/28
 * @Discription:
 **/
@ApiModel
public class InvestRecordInfoVo {

    @ApiModelProperty(value = "用户ID",example = "0")
    private Integer userId;
    @ApiModelProperty(value = "投资资产ID",example = "0")
    private Integer investId;
    @ApiModelProperty(value = "投资记录")
    private List<InvestRecordItemInfoVo> items;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getInvestId() {
        return investId;
    }

    public void setInvestId(Integer investId) {
        this.investId = investId;
    }

    public List<InvestRecordItemInfoVo> getItems() {
        return items;
    }

    public void setItems(List<InvestRecordItemInfoVo> items) {
        this.items = items;
    }
}
