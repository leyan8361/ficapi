package com.fic.service.service;

import com.fic.service.Vo.*;
import com.fic.service.entity.BalanceStatement;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/5
 *   @Discription:
**/
public interface BalanceService {

     TradeRecordInfoVo getTradeRecord(Integer userId);

     ResponseVo getTradeRecordV2(TradeRecordRequestVo tradeRecordRequestVo);

     ResponseVo reward(RewardInfoVo rewardInfoVo);

     List<RewardRecordInfoVo> getReward(Integer userId);

}
