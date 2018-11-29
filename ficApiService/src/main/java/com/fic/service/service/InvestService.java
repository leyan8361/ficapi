package com.fic.service.service;

import com.fic.service.Vo.InvestInfoVo;
import com.fic.service.Vo.InvestRecordInfoVo;
import com.fic.service.entity.Invest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:
**/
public interface InvestService {

       int invest(Invest invest, InvestInfoVo investInfoVo, BigDecimal investBalance);

       InvestRecordInfoVo getInvestDetail(Integer userId);
}