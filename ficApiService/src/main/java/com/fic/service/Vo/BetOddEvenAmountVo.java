package com.fic.service.Vo;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2018/12/19
 *   @Discription:
**/
public class BetOddEvenAmountVo {

    private BigDecimal oddFic;

    private BigDecimal evenFic;

    public BigDecimal getOddFic() {
        return oddFic;
    }

    public void setOddFic(BigDecimal oddFic) {
        this.oddFic = oddFic;
    }

    public BigDecimal getEvenFic() {
        return evenFic;
    }

    public void setEvenFic(BigDecimal evenFic) {
        this.evenFic = evenFic;
    }
}
