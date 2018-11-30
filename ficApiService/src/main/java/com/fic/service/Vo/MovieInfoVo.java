package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class MovieInfoVo {

    @ApiModelProperty(value = "电影ID")
    private int movieId;
    @ApiModelProperty(value = "电影Name")
    private String movieName;
    @ApiModelProperty(value = "电影简介")
    private String movieRemark;
    @ApiModelProperty(value = "电影封面")
    private String movieCoverUrl;
    @ApiModelProperty(value = "投资人数")
    private int investCount;
    @ApiModelProperty(value = "投资金额")
    private BigDecimal investTotalAmount;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieRemark() {
        return movieRemark;
    }

    public void setMovieRemark(String movieRemark) {
        this.movieRemark = movieRemark;
    }

    public String getMovieCoverUrl() {
        return movieCoverUrl;
    }

    public void setMovieCoverUrl(String movieCoverUrl) {
        this.movieCoverUrl = movieCoverUrl;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public BigDecimal getInvestTotalAmount() {
        return investTotalAmount;
    }

    public void setInvestTotalAmount(BigDecimal investTotalAmount) {
        this.investTotalAmount = investTotalAmount;
    }
}
