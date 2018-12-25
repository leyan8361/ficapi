package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetRecordInfoVo {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "竞猜记录")
    private List<BetRecordVo> items;

    @ApiModelProperty(value = "连续竞猜奖励")
    private BigDecimal continueBetReward;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<BetRecordVo> getItems() {
        return items;
    }

    public void setItems(List<BetRecordVo> items) {
        this.items = items;
    }

    public BigDecimal getContinueBetReward() {
        return continueBetReward;
    }

    public void setContinueBetReward(BigDecimal continueBetReward) {
        this.continueBetReward = continueBetReward;
    }
}
