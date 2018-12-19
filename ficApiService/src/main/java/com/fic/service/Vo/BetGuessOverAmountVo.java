package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;


public class BetGuessOverAmountVo {

    private BigDecimal canFic;

    private BigDecimal couldntFic;

    public BigDecimal getCanFic() {
        return canFic;
    }

    public void setCanFic(BigDecimal canFic) {
        this.canFic = canFic;
    }

    public BigDecimal getCouldntFic() {
        return couldntFic;
    }

    public void setCouldntFic(BigDecimal couldntFic) {
        this.couldntFic = couldntFic;
    }
}
