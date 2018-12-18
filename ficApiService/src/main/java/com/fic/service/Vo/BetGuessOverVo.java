package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetGuessOverVo {

    @ApiModelProperty(value = "当初级场时,此项为选能人数")
    private Integer canCount;

    @ApiModelProperty(value = "当初级场时,此项为选不人数")
    private Integer couldntCount;

    public Integer getCanCount() {
        return canCount;
    }

    public void setCanCount(Integer canCount) {
        this.canCount = canCount;
    }

    public Integer getCouldntCount() {
        return couldntCount;
    }

    public void setCouldntCount(Integer couldntCount) {
        this.couldntCount = couldntCount;
    }
}
