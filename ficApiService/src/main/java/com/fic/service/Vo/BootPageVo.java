package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BootPageVo {

    @ApiModelProperty("引导页ID")
    private Integer id;

    @ApiModelProperty("引导页URL")
    private String bootPageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBootPageUrl() {
        return bootPageUrl;
    }

    public void setBootPageUrl(String bootPageUrl) {
        this.bootPageUrl = bootPageUrl;
    }
}
