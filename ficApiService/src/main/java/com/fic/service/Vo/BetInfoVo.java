package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/17
 *   @Discription: app request -- > scence(项目) ===> movie(电影) ====> scence_movie(竞猜场次) =====> box(票房，奖池，投注信息)
**/
@ApiModel
public class BetInfoVo {

    @ApiModelProperty(value = "项目ID")
    private Integer id;

    @ApiModelProperty(value = "项目名称",required = true)
    private String betName;

    @ApiModelProperty(value = "项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房)",required = true)
    private Byte betType;

    @ApiModelProperty(value = "(0，下架)(1,上架)，默认下架")
    private Byte status;

    @ApiModelProperty(value = "连续投注次数")
    private int continueBetTime;

    @ApiModelProperty(value = "连续竞猜奖励")
    private BigDecimal continueBetReward;

    @ApiModelProperty(value = "奖励池剩余预备金")
    private BigDecimal totalJasckpot;

    @ApiModelProperty(value = "竞猜电影列表(未开奖的)")
    private List<BetMovieInfoVo> movieItem;

    @ApiModelProperty(value = "竞猜电影列表(开奖了的)")
    private List<BetMovieDrawVo> drawMovieItem;

    public List<BetMovieInfoVo> getMovieItem() {
        return movieItem;
    }

    public void setMovieItem(List<BetMovieInfoVo> movieItem) {
        this.movieItem = movieItem;
    }

    public List<BetMovieDrawVo> getDrawMovieItem() {
        return drawMovieItem;
    }

    public void setDrawMovieItem(List<BetMovieDrawVo> drawMovieItem) {
        this.drawMovieItem = drawMovieItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBetName() {
        return betName;
    }

    public void setBetName(String betName) {
        this.betName = betName;
    }

    public Byte getBetType() {
        return betType;
    }

    public void setBetType(Byte betType) {
        this.betType = betType;
    }

    public int getContinueBetTime() {
        return continueBetTime;
    }

    public void setContinueBetTime(int continueBetTime) {
        this.continueBetTime = continueBetTime;
    }

    public BigDecimal getTotalJasckpot() {
        return totalJasckpot;
    }

    public void setTotalJasckpot(BigDecimal totalJasckpot) {
        this.totalJasckpot = totalJasckpot;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public BigDecimal getContinueBetReward() {
        return continueBetReward;
    }

    public void setContinueBetReward(BigDecimal continueBetReward) {
        this.continueBetReward = continueBetReward;
    }

}
