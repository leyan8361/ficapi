package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class UploadHeadImageInfoVo {

    @ApiModelProperty(value = "头像URL", example = "0")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UploadHeadImageInfoVo(String url) {
        this.url = url;
    }
}
