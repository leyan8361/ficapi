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
    private List<MovieInfoVo> dividendList;

    @ApiModelProperty("已杀青，待开机 电影列表")
    private List<MovieInfoVo> investList;

    public List<MovieInfoVo> getDividendList() {
        return dividendList;
    }

    public void setDividendList(List<MovieInfoVo> dividendList) {
        this.dividendList = dividendList;
    }

    public List<MovieInfoVo> getInvestList() {
        return investList;
    }

    public void setInvestList(List<MovieInfoVo> investList) {
        this.investList = investList;
    }
}
