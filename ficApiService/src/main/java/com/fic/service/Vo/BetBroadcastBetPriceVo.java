package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetBroadcastBetPriceVo {

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("奖金")
    private String price;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
