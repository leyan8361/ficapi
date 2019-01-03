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
public class MovieDividendVo {

    @ApiModelProperty("电影ID")
    private Integer movieId;

    @ApiModelProperty("电影名称")
    private String movieName;
    @ApiModelProperty("电影类型")
    private String movieType;
    @ApiModelProperty("总预算")
    private BigDecimal budget;
    @ApiModelProperty("封面url")
    private String movieCoverUrl;
    @ApiModelProperty("(0，已杀青)(1，待开机)(2,已分红)(3,待分红)")
    private Integer status;
    @ApiModelProperty("票房")
    private String boxInfo;
    @ApiModelProperty(value = "回报率",example = "0")
    private BigDecimal returnRate;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getMovieCoverUrl() {
        return movieCoverUrl;
    }

    public void setMovieCoverUrl(String movieCoverUrl) {
        this.movieCoverUrl = movieCoverUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(String boxInfo) {
        this.boxInfo = boxInfo;
    }

    public BigDecimal getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(BigDecimal returnRate) {
        this.returnRate = returnRate;
    }
}
