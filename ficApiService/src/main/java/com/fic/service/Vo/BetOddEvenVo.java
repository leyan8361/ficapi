package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class BetOddEvenVo {

    @ApiModelProperty(value = "当猜单双时,此荐为选单人数")
    private Integer oddCount;

    @ApiModelProperty(value = "当猜单双时,此荐为选双人数")
    private Integer evenCount;

    public Integer getOddCount() {
        if(null == oddCount){
            return 0;
        }
        return oddCount;
    }

    public void setOddCount(Integer oddCount) {
        this.oddCount = oddCount;
    }

    public Integer getEvenCount() {
        if(null == evenCount){
            return 0;
        }
        return evenCount;
    }

    public void setEvenCount(Integer evenCount) {
        this.evenCount = evenCount;
    }

    @Override
    public String toString() {
        return "BetOddEvenVo{" +
                "oddCount=" + oddCount +
                ", evenCount=" + evenCount +
                '}';
    }
}
