package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *   @Author Xie
 *   @Date 2018/11/30
 *   @Discription:
**/
@ApiModel
public class UpdateUserInfoVo {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
    @ApiModelProperty(value = "手机号")
    private String telephone;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "邮编", required = true)
    private String zip;
}
