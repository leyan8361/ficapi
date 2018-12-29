package com.fic.service.Vo;

import com.fic.service.entity.ActorInfo;
import com.fic.service.entity.Movie;
import com.fic.service.entity.MovieDetailInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class MoiveAddInfoVo{

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
    private int status;
    @ApiModelProperty("责任描述")
    private String dutyDescription;
    @ApiModelProperty("票房")
    private String boxInfo;
    @ApiModelProperty(value = "周期(月)",example = "2")
    private Integer investCycle;
    @ApiModelProperty("新增修改不需要填写, 参投人数")
    private Integer investCount;
    @ApiModelProperty(value = "影片时长",example = "120")
    private Integer movieLast;
    @ApiModelProperty(value = "回报率",example = "0")
    private BigDecimal returnRate;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDutyDescription() {
        return dutyDescription;
    }

    public void setDutyDescription(String dutyDescription) {
        this.dutyDescription = dutyDescription;
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

    public Integer getInvestCount() {
        return investCount;
    }

    public void setInvestCount(Integer investCount) {
        this.investCount = investCount;
    }

    public Integer getMovieLast() {
        return movieLast;
    }

    public void setMovieLast(Integer movieLast) {
        this.movieLast = movieLast;
    }

    public BigDecimal getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(BigDecimal returnRate) {
        this.returnRate = returnRate;
    }
}
