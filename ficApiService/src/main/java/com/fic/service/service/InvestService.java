package com.fic.service.service;

import com.fic.service.Vo.InvestInfoVo;
import com.fic.service.entity.Invest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:
**/
public interface InvestService {

       Boolean invest(Invest invest, InvestInfoVo investInfoVo, BigDecimal investBalance);

}
