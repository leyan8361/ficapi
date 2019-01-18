package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BetRankingRecordVo {

    @ApiModelProperty("电影名称")
    private String movieName;

    @ApiModelProperty("下注类型(0，单)(1,双)")
    private int betWhich;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getBetWhich() {
        return betWhich;
    }

    public void setBetWhich(int betWhich) {
        this.betWhich = betWhich;
    }
}
