package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

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
    @ApiModelProperty(value = "电影类型")
    private String movieType;
    @ApiModelProperty(value = "总预算(亿)")
    private BigDecimal budget;
    @ApiModelProperty(value = "开放额度(万元)")
    private BigDecimal quota;
    @ApiModelProperty(value = "上映地点")
    private String showPlace;
    @ApiModelProperty(value = "上映时间")
    private Date showTime;

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

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public String getShowPlace() {
        return showPlace;
    }

    public void setShowPlace(String showPlace) {
        this.showPlace = showPlace;
    }

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }
}
