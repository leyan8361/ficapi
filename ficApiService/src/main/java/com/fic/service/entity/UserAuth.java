package com.fic.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class UserAuth {
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;
    @ApiModelProperty("身份证ID")
    private String cerId;
    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("身份证正面")
    private String frontFaceUrl;
    @ApiModelProperty("身份证反面")
    private String backFaceUrl;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @ApiModelProperty("(0，待审核)(1,审核成功)(2,审核失败)")
    private Integer status;
    @ApiModelProperty("备注")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCerId() {
        return cerId;
    }

    public void setCerId(String cerId) {
        this.cerId = cerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrontFaceUrl() {
        return frontFaceUrl;
    }

    public void setFrontFaceUrl(String frontFaceUrl) {
        this.frontFaceUrl = frontFaceUrl;
    }

    public String getBackFaceUrl() {
        return backFaceUrl;
    }

    public void setBackFaceUrl(String backFaceUrl) {
        this.backFaceUrl = backFaceUrl;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}