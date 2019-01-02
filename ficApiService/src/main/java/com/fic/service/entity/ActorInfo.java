package com.fic.service.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

@ApiModel
public class ActorInfo {

    private Integer id;
    @ApiModelProperty(value = "电影ID")
    private int movieId;
    @ApiModelProperty(value = "角色",example = "导演")
    private String role;
    @ApiModelProperty(value = "角色名称",example = "成龙")
    private String roleName;
    @ApiModelProperty(value = "角色封面url")
    private String roleCoverUrl;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
