package com.fic.service.Vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LuckTurntableStaticsVo {

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("用户电话号码")
    private String telephone;

    @ApiModelProperty("总抽奖次数")
    private Integer totalDraw;

    @ApiModelProperty("各项中奖情况")
    private String bingoStr;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getTotalDraw() {
        return totalDraw;
    }

    public void setTotalDraw(Integer totalDraw) {
        this.totalDraw = totalDraw;
    }

    public String getBingoStr() {
        return bingoStr;
    }

    public void setBingoStr(String bingoStr) {
        this.bingoStr = bingoStr;
    }
}
