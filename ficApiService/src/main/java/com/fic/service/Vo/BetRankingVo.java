package com.fic.service.Vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class BetRankingVo {

    @ApiModelProperty("用户ID")
    private int userId;

    @ApiModelProperty("头像URL")
    private String himageUrl;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("级别")
    private int level;

    @ApiModelProperty("胜率")
    private BigDecimal winRate;

    @ApiModelProperty("参与天数")
    private int playInDay;

    @ApiModelProperty("今日竞猜")
    private List<BetRankingRecordVo> playRecord;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHimageUrl() {
        return himageUrl;
    }

    public void setHimageUrl(String himageUrl) {
        this.himageUrl = himageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public BigDecimal getWinRate() {
        return winRate;
    }

    public void setWinRate(BigDecimal winRate) {
        this.winRate = winRate;
    }

    public int getPlayInDay() {
        return playInDay;
    }

    public void setPlayInDay(int playInDay) {
        this.playInDay = playInDay;
    }

    public List<BetRankingRecordVo> getPlayRecord() {
        if(null == playRecord){
            return new ArrayList<>();
        }
        return playRecord;
    }

    public void setPlayRecord(List<BetRankingRecordVo> playRecord) {
        this.playRecord = playRecord;
    }
}
