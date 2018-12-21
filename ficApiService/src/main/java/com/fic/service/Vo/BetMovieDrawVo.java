package com.fic.service.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(value = "场次ID")
    private Integer scenceMovieId;

    @ApiModelProperty(value = "竞猜电影封面URL")
    private String betMovieCoverUrl;

    @ApiModelProperty(value = "按SumDay统计票房 单位万")
    private String boxInfo;

    @ApiModelProperty(value = "总票房 包含单位")
    private String sumBoxInfo;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "票房统计日期")
    private Date sumDay;

    @ApiModelProperty(value = "status, (0,待开奖)(1,已开奖)(2,已退还关闭)(3,关闭无人投注)")
    private byte status;

    @ApiModelProperty(value = "开奖结果 (0,猜单双，单)(1,猜单双，双)(2，猜票房能不能，能)(3,猜票房能不能，不能)(4,选择题A)(5,选择题B)(6,选择题C)(7,选择题D)")
    private String drawResult;

    public String getDrawResult() {
        return drawResult;
    }

    public void setDrawResult(String drawResult) {
        this.drawResult = drawResult;
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

    public String getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(String boxInfo) {
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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
