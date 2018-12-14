package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *   @Author Xie
 *   @Date 2018/12/14
 *   @Discription:
**/
@ApiModel
public class BannerInfoVo {

    @ApiModelProperty(value = "BannerID")
    private Integer id;

    @ApiModelProperty(value = "链接URL")
    private String bannerUrl;

    @ApiModelProperty(value = "顺序,int")
    private Integer inOrder;

    @ApiModelProperty(value = "android 跳转URL")
    private String jumpUrlAndroid;

    @ApiModelProperty(value = "ios 跳转URL")
    private String jumpUrlIos;

    @ApiModelProperty(value = "状态,(0，未上架)(1上架)")
    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Integer getInOrder() {
        return inOrder;
    }

    public void setInOrder(Integer inOrder) {
        this.inOrder = inOrder;
    }

    public String getJumpUrlAndroid() {
        return jumpUrlAndroid;
    }

    public void setJumpUrlAndroid(String jumpUrlAndroid) {
        this.jumpUrlAndroid = jumpUrlAndroid;
    }

    public String getJumpUrlIos() {
        return jumpUrlIos;
    }

    public void setJumpUrlIos(String jumpUrlIos) {
        this.jumpUrlIos = jumpUrlIos;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
