package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AddressVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("币种")
    private String coinType;

    @ApiModelProperty("(0,淘影生成不可修改删除)(1,用户添加可修改删除)")
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
