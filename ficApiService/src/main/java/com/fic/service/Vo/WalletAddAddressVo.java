package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *   @Author Xie
 *   @Date 2019/1/29
 *   @Discription:
**/
@ApiModel
public class WalletAddAddressVo {

    @ApiModelProperty(value = "用户ID",required = true)
    private Integer userId;

    @ApiModelProperty(value = "币种",required = true,example = "TFC")
    private String coinType;

    @ApiModelProperty(value = "地址",required = true)
    private String address;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
