package com.fic.service.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MovieInvestVo {

    @ApiModelProperty("新增修改不需要填写,电影ID")
    private Integer movieId;

    @ApiModelProperty("电影名称")
    private String movieName;
    @ApiModelProperty("电影类型")
    private String movieType;
    @ApiModelProperty("总预算")
    private BigDecimal budget;
    @ApiModelProperty("开放额度")
    private BigDecimal quota;
    @ApiModelProperty("封面url")
    private String movieCoverUrl;
    @ApiModelProperty("上映地点")
    private String showPlace;
    @ApiModelProperty("上映时间")
    private String showTime;
    @ApiModelProperty("(0，已杀青)(1，待开机)(2,已分红)(3,待分红)")
    private Integer status;
    @ApiModelProperty("票房")
    private String boxInfo;
    @ApiModelProperty(value = "周期(月)",example = "2")
    private Integer investCycle;

    @ApiModelProperty(value = "投资金额")
    private BigDecimal investTotalAmount;
    @ApiModelProperty("责任描述")
    private String [] dutyDescriptionArray;

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

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public String getMovieCoverUrl() {
        return movieCoverUrl;
    }

    public void setMovieCoverUrl(String movieCoverUrl) {
        this.movieCoverUrl = movieCoverUrl;
    }

    public String getShowPlace() {
        return showPlace;
    }

    public void setShowPlace(String showPlace) {
        this.showPlace = showPlace;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
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

    public Integer getInvestCycle() {
        return investCycle;
    }

    public void setInvestCycle(Integer investCycle) {
        this.investCycle = investCycle;
    }

    public BigDecimal getInvestTotalAmount() {
        return investTotalAmount;
    }

    public void setInvestTotalAmount(BigDecimal investTotalAmount) {
        this.investTotalAmount = investTotalAmount;
    }

    public String[] getDutyDescriptionArray() {
        return dutyDescriptionArray;
    }

    public void setDutyDescriptionArray(String[] dutyDescriptionArray) {
        this.dutyDescriptionArray = dutyDescriptionArray;
    }
}
