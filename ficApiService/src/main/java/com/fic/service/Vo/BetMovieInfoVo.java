package com.fic.service.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetMovieInfoVo {

    @ApiModelProperty(value = "竞猜电影ID")
    private Integer id;

    @ApiModelProperty(value = "场次ID")
    private Integer scenceMovieId;

    @ApiModelProperty(value = "竞猜电影名称",required = true)
    private String betMovieName;

    @ApiModelProperty(value = "竞猜电影封面URL")
    private String betMovieCoverUrl;

    @ApiModelProperty(value = "上架时间",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activityTime;

    @ApiModelProperty(value = "下架时间",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date disabledTime;

    @ApiModelProperty(value = "电影类型",example = "动作、冒险",required = true)
    private String movieType;

    @ApiModelProperty(value = "导演",example = "温子仁",required = true)
    private String movieDirector;

    @ApiModelProperty(value = "上映日期",example = "2018-12-14",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date showTime;

    @ApiModelProperty(value = "按SumDay统计票房 单位万")
    private BigDecimal boxInfo;

    @ApiModelProperty(value = "总票房 包含单位")
    private String sumBoxInfo;

    @ApiModelProperty(value = "票房统计日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sumDay;

    @ApiModelProperty(value = "投注人数, 以scence项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房)区分解析vo, BetOddEvenVo,BetGuessOverVo,BetChoiceVo")
    private Object betCountVo;

    public Object getBetCountVo() {
        return betCountVo;
    }

    public void setBetCountVo(Object betCountVo) {
        this.betCountVo = betCountVo;
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

    public BigDecimal getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(BigDecimal boxInfo) {
        this.boxInfo = boxInfo;
    }

    public String getSumBoxInfo() {
        return sumBoxInfo;
    }

    public void setSumBoxInfo(String sumBoxInfo) {
        this.sumBoxInfo = sumBoxInfo;
    }

    public Date getSumDay() {
        return sumDay;
    }

    public void setSumDay(Date sumDay) {
        this.sumDay = sumDay;
    }

    public Integer getScenceMovieId() {
        return scenceMovieId;
    }

    public void setScenceMovieId(Integer scenceMovieId) {
        this.scenceMovieId = scenceMovieId;
    }
}
