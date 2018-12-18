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
public class BetMovieVo {

    @ApiModelProperty(value = "竞猜电影ID")
    private Integer id;

    @ApiModelProperty(value = "竞猜电影名称",required = true)
    private String betMovieName;

    @ApiModelProperty(value = "竞猜电影封面URL")
    private String betMovieCoverUrl;

    @ApiModelProperty(value = "上架时间",required = true,example = "2018-12-18")
    private Date activityTime;

    @ApiModelProperty(value = "下架时间",required = true,example = "2018-12-18")
    private Date disabledTime;

    @ApiModelProperty(value = "电影类型",example = "动作、冒险",required = true)
    private String movieType;

    @ApiModelProperty(value = "导演",example = "温子仁",required = true)
    private String movieDirector;

    @ApiModelProperty(value = "上映日期",example = "2018-12-14",required = true)
    private Date showTime;

    @ApiModelProperty(value = "今日票房 单位万")
    private BigDecimal boxInfo;

    @ApiModelProperty(value = "总票房 包含单位")
    private String sumBoxInfo;

    @ApiModelProperty(value = "票房统计日期",example = "2018-12-14")
    private Date sumDay;

    public BigDecimal getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(BigDecimal boxInfo) {
        this.boxInfo = boxInfo;
    }

    public Date getSumDay() {
        return sumDay;
    }

    public void setSumDay(Date sumDay) {
        this.sumDay = sumDay;
    }

    public String getSumBoxInfo() {
        return sumBoxInfo;
    }

    public void setSumBoxInfo(String sumBoxInfo) {
        this.sumBoxInfo = sumBoxInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBetMovieName() {
        return betMovieName;
    }

    public void setBetMovieName(String betMovieName) {
        this.betMovieName = betMovieName;
    }

    public String getBetMovieCoverUrl() {
        return betMovieCoverUrl;
    }

    public void setBetMovieCoverUrl(String betMovieCoverUrl) {
        this.betMovieCoverUrl = betMovieCoverUrl;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }

    public Date getDisabledTime() {
        return disabledTime;
    }

    public void setDisabledTime(Date disabledTime) {
        this.disabledTime = disabledTime;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }


}
