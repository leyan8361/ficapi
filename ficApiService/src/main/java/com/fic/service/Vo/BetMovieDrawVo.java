package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
/**
 *   @Author Xie
 *   @Date 2018/12/17
 *   @Discription:
**/
@ApiModel
public class BetMovieDrawVo {

    @ApiModelProperty(value = "竞猜电影ID")
    private Integer id;

    @ApiModelProperty(value = "竞猜电影名称",required = true)
    private String betMovieName;

    @ApiModelProperty(value = "竞猜电影封面URL")
    private String betMovieCoverUrl;

    @ApiModelProperty(value = "按SumDay统计票房 单位万")
    private BigDecimal boxInfo;

    @ApiModelProperty(value = "总票房 包含单位")
    private String sumBoxInfo;

    @ApiModelProperty(value = "票房统计日期")
    private Date sumDay;

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
}
