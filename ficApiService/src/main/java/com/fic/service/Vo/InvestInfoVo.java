package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Author Xie
 * @Date 2018/11/27
 * @Discription: 投资入参
 **/
@ApiModel
public class InvestInfoVo {

    @ApiModelProperty(value = "用户ID", required = true, example = "0")
    private Integer userId;
    @ApiModelProperty(value = "电影ID", required = true, example = "0")
    private Integer moveId;
    @ApiModelProperty(value = "电影名", required = true)
    private String moveName;
    @ApiModelProperty(value = "投资额度 FIC数量", required = true)
    private BigDecimal amount;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMoveId() {
        return moveId;
    }

    public void setMoveId(Integer moveId) {
        this.moveId = moveId;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
