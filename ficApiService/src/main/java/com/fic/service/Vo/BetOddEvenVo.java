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


}
