package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class BetSignVo {

    @ApiModelProperty("签到数组")
    private int [] betTime;

    @ApiModelProperty("上周连续奖励金")
    private BigDecimal continueBetReward;

    public int[] getBetTime() {
        return betTime;
    }

    public void setBetTime(int[] betTime) {
        this.betTime = betTime;
    }

    public BigDecimal getContinueBetReward() {
        return continueBetReward;
    }

    public void setContinueBetReward(BigDecimal continueBetReward) {
        this.continueBetReward = continueBetReward;
    }
}
