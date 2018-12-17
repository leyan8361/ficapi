package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetChoiceVo {

    @ApiModelProperty(value = "当中级场时,此荐为选A人数")
    private Integer aChoice;

    @ApiModelProperty(value = "当中级场时,此荐为选B人数")
    private Integer bChoice;

    @ApiModelProperty(value = "当中级场时,此荐为选C人数")
    private Integer cChoice;

    @ApiModelProperty(value = "当中级场时,此荐为选D人数")
    private Integer dChoice;

    public Integer getaChoice() {
        return aChoice;
    }

    public void setaChoice(Integer aChoice) {
        this.aChoice = aChoice;
    }

    public Integer getbChoice() {
        return bChoice;
    }

    public void setbChoice(Integer bChoice) {
        this.bChoice = bChoice;
    }

    public Integer getcChoice() {
        return cChoice;
    }

    public void setcChoice(Integer cChoice) {
        this.cChoice = cChoice;
    }

    public Integer getdChoice() {
        return dChoice;
    }

    public void setdChoice(Integer dChoice) {
        this.dChoice = dChoice;
    }
}
