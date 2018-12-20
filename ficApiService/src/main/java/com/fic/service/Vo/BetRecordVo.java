package com.fic.service.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/12/20
 *   @Discription:
**/
@ApiModel
public class BetRecordVo {

    @ApiModelProperty(value = "竞猜记录ID")
    private Integer id;

    @ApiModelProperty(value = "竞猜电影名称",required = true)
    private String betMovieName;

//    @ApiModelProperty(value = "竞猜电影封面URL")
//    private String betMovieCoverUrl;

    @ApiModelProperty(value = "下注时间",example = "2018-12-18 10:45")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date createdTime;

    @ApiModelProperty(value = "下注金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "服务费加奖励金")
    private BigDecimal fee;

    @ApiModelProperty(value = "赔率")
    private BigDecimal odds;

    @ApiModelProperty(value = "是否中奖")
    private boolean bingo;

    @ApiModelProperty(value = "奖金总计")
    private BigDecimal totalReward;

    @ApiModelProperty(value = "投注项")
    private String betWhich;

    @ApiModelProperty(value = "竞猜项目类型")
    private int betType;

    @ApiModelProperty(value = "开奖结果")
    private String drawResult;

    @ApiModelProperty(value = "连续奖励金")
    private BigDecimal continueBetReward;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getOdds() {
        return odds;
    }

    public void setOdds(BigDecimal odds) {
        this.odds = odds;
    }

    public boolean isBingo() {
        return bingo;
    }

    public void setBingo(boolean bingo) {
        this.bingo = bingo;
    }

    public BigDecimal getTotalReward() {
        return totalReward;
    }

    public void setTotalReward(BigDecimal totalReward) {
        this.totalReward = totalReward;
    }

    public String getBetWhich() {
        return betWhich;
    }

    public void setBetWhich(String betWhich) {
        this.betWhich = betWhich;
    }

    public int getBetType() {
        return betType;
    }

    public void setBetType(int betType) {
        this.betType = betType;
    }

    public String getDrawResult() {
        return drawResult;
    }

    public void setDrawResult(String drawResult) {
        this.drawResult = drawResult;
    }

    public String getBetMovieName() {
        return betMovieName;
    }

    public void setBetMovieName(String betMovieName) {
        this.betMovieName = betMovieName;
    }

    public BigDecimal getContinueBetReward() {
        return continueBetReward;
    }

    public void setContinueBetReward(BigDecimal continueBetReward) {
        this.continueBetReward = continueBetReward;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
