package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class MovieVo {

    @ApiModelProperty("已分红、待分红 电影列表")
    private List<MovieDividendVo> dividendList;

    @ApiModelProperty("已杀青，待开机 电影列表")
    private List<MovieInvestVo> investList;

    public List<MovieDividendVo> getDividendList() {
        return dividendList;
    }

    public void setDividendList(List<MovieDividendVo> dividendList) {
        this.dividendList = dividendList;
    }

    public List<MovieInvestVo> getInvestList() {
        return investList;
    }

    public void setInvestList(List<MovieInvestVo> investList) {
        this.investList = investList;
    }
}
