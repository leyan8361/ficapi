package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class InvestSuccessInfoVo {

    @ApiModelProperty(value = "第N位投资人",example = "0")
    private Integer rankingOfInvest;

    public Integer getRankingOfInvest() {
        return rankingOfInvest;
    }

    public void setRankingOfInvest(Integer rankingOfInvest) {
        this.rankingOfInvest = rankingOfInvest;
    }
}
