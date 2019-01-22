package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetBroadcastVo {

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("奖金or奖品，当是奖金时，范例(100TFC)，当是奖品时，显示奖品名称")
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
