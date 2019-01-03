package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MovieDetailInfoVo extends MovieInfoVo{

    @ApiModelProperty(value = "点赞(0,未点赞)(1,已点赞)")
    private int likz;
    @ApiModelProperty(value = "收藏(0,未收藏)(1,已收藏)")
    private int fav;
    @ApiModelProperty(value = "点赞人数")
    private int countAlike;
    @ApiModelProperty(value = "收藏人数")
    private int countCollect;


    public int getLikz() {
        return likz;
    }

    public void setLikz(int likz) {
        this.likz = likz;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getCountAlike() {
        return countAlike;
    }

    public void setCountAlike(int countAlike) {
        this.countAlike = countAlike;
    }

    public int getCountCollect() {
        return countCollect;
    }

    public void setCountCollect(int countCollect) {
        this.countCollect = countCollect;
    }
}
