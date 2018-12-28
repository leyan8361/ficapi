package com.fic.service.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ActorInfo {

    @ApiModelProperty(value = "角色",example = "导演")
    private String role;
    @ApiModelProperty(value = "角色名称",example = "成龙")
    private String roleName;
    @ApiModelProperty(value = "角色封面url")
    private String roleCoverUrl;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCoverUrl() {
        return roleCoverUrl;
    }

    public void setRoleCoverUrl(String roleCoverUrl) {
        this.roleCoverUrl = roleCoverUrl;
    }
}
