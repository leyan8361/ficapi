package com.fic.service.Vo;

import com.fic.service.entity.Movie;
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
public class MovieInfoVo extends Movie {

    @ApiModelProperty(value = "投资金额")
    private BigDecimal investTotalAmount;
    @ApiModelProperty("责任描述")
    private String [] dutyDescriptionArray;

    public String[] getDutyDescriptionArray() {
        return dutyDescriptionArray;
    }

    public void setDutyDescriptionArray(String[] dutyDescriptionArray) {
        this.dutyDescriptionArray = dutyDescriptionArray;
    }

    public BigDecimal getInvestTotalAmount() {
        return investTotalAmount;
    }

    public void setInvestTotalAmount(BigDecimal investTotalAmount) {
        this.investTotalAmount = investTotalAmount;
    }
}
