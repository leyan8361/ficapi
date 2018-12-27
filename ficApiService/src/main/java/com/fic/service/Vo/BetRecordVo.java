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

    @ApiModelProperty(value = "投注获取奖励")
    private BigDecimal addedPrice;

    @ApiModelProperty(value = "赔率")
    private BigDecimal odds;

    @ApiModelProperty(value = "总计奖励")
    private BigDecimal bingoPrice;

//    @ApiModelProperty(value = "特殊情况预备备金赔付")
//    private BigDecimal returningAmount;

    @ApiModelProperty(value = "(0,待开奖)(1，已中奖)(2，未中奖)(3，极端情况，启用备用金，返还竞猜金额)(4，备用金不足以赔付，运营后台人工干预)")
    private byte bingo;

    @ApiModelProperty(value = "投注项")
    private String betWhich;

    @ApiModelProperty(value = "竞猜 项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房)")
    private int betType;

    @ApiModelProperty(value = "开奖结果 (0,猜单双，单)(1,猜单双，双)(2，猜票房能不能，能)(3,猜票房能不能，不能)(4,选择题A)(5,选择题B)(6,选择题C)(7,选择题D)")
    private String drawResult;

    @ApiModelProperty(value = "开奖结果辅助 (当betType为单双时，此项为票房个位)(当betType其他时，此项为票房金额[包含单位])")
    private String drawResultHelper;

//    @ApiModelProperty(value = "连续奖励金")
//    private BigDecimal continueBetReward;

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

    public byte getBingo() {
        return bingo;
    }

    public void setBingo(byte bingo) {
        this.bingo = bingo;
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

//    public BigDecimal getContinueBetReward() {
//        return continueBetReward;
//    }
//
//    public void setContinueBetReward(BigDecimal continueBetReward) {
//        this.continueBetReward = continueBetReward;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBingoPrice() {
        return bingoPrice;
    }

    public void setBingoPrice(BigDecimal bingoPrice) {
        this.bingoPrice = bingoPrice;
    }

    public String getDrawResultHelper() {
        return drawResultHelper;
    }

    public void setDrawResultHelper(String drawResultHelper) {
        this.drawResultHelper = drawResultHelper;
    }

    public BigDecimal getAddedPrice() {
        return addedPrice;
    }

    public void setAddedPrice(BigDecimal addedPrice) {
        this.addedPrice = addedPrice;
    }
}
