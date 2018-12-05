package com.fic.service.service;

import com.fic.service.Vo.TradeRecordInfoVo;
import com.fic.service.Vo.TradeRecordVo;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/5
 *   @Discription:
**/
public interface BalanceService {

     TradeRecordInfoVo getTradeRecord(Integer userId);

}
