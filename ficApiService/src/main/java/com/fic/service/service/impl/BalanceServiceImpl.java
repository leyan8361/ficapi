package com.fic.service.service.impl;

import com.fic.service.Enum.BalanceStatementTypeEnum;
import com.fic.service.Enum.FinanceWayEnum;
import com.fic.service.Vo.TradeRecordVo;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.BalanceService;
import com.fic.service.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/5
 *   @Discription:
**/
@Service
public class BalanceServiceImpl implements BalanceService {

    private final Logger log = LoggerFactory.getLogger(BalanceServiceImpl.class);

    @Autowired
    BalanceStatementMapper balanceStatementMapper;
    @Autowired
    MovieMapper movieMapper;
    @Autowired
    InvestDetailMapper investDetailMapper;
    @Autowired
    DistributionMapper distributionMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<TradeRecordVo> getTradeRecord(Integer userId) {
        List<TradeRecordVo> tradeRecordVoList = new ArrayList<>();
        List<TradeRecordVo> inviteRecordList = new ArrayList<>();
        List<TradeRecordVo> investRecordList = new ArrayList<>();
        List<TradeRecordVo> investMoveRecordList = new ArrayList<>();
        List<BalanceStatement> statementList = balanceStatementMapper.findAllByUserId(userId);
        if(statementList.size() <= 0)return tradeRecordVoList;

        for(BalanceStatement statement: statementList){
            if(BalanceStatementTypeEnum.REWARD.getCode().equals(statement.getType())){
                String userPhone = userMapper.getUserNameByUserId(statement.getUserId());
                if(StringUtils.isEmpty(userPhone))continue;

                TradeRecordVo tradeRecordVo = new TradeRecordVo();
                tradeRecordVo.setUserId(statement.getUserId());
                tradeRecordVo.setTelephone(RegexUtil.replaceTelephone(userPhone));
                tradeRecordVo.setWay(statement.getWay());
                tradeRecordVo.setAmount(statement.getAmount());
                tradeRecordVo.setType(statement.getType());
                /**投资电影*/
                if(BalanceStatementTypeEnum.INVEST.getCode().equals(statement.getType())){
                    String moveName = investDetailMapper.findMoveNameByDetailId(statement.getInvestDetailId());
                    tradeRecordVo.setMoveName(moveName);
                }
                /**分销*/
                if(BalanceStatementTypeEnum.REWARD.getCode().equals(statement.getType())){
                    Distribution distribution = distributionMapper.selectByPrimaryKey(statement.getDistributionId());
                    if(null != distribution){
                        //TODO 无法判断间接 直接，注册投资，细分 5 奖励
                    }
                }
            }
        }


        return null;
    }





    /**
     * 处理佣金
//     */
//            if(BalanceStatementTypeEnum.REWARD.getCode().equals(statement.getType())){
//        List<Distribution> distributionList = distributionMapper.findAllByUserId(statement.getUserId());
//        if(distributionList.size() > 0){
//            for(Distribution distribution : distributionList){
//                /**一级分销*/
//                String levelOneUserPhone = userMapper.getUserNameByUserId(distribution.getDisLevelOneUserId());
//                if(StringUtils.isNotEmpty(levelOneUserPhone)){
//                    /**注册*/
//                    if(null != distribution.getInviteRewardOne()){
//                        TradeRecordVo inviteOneRecord = new TradeRecordVo();
//                        inviteOneRecord.setType(BalanceStatementTypeEnum.REWARD.getCode());
//                        inviteOneRecord.setAmount(distribution.getInviteRewardOne());
//                        inviteOneRecord.setWay(FinanceWayEnum.IN.getCode());
//                        inviteOneRecord.setTelephone(RegexUtil.replaceTelephone(levelOneUserPhone));
//                        inviteOneRecord.setUserId(distribution.getUserId());
//                        inviteRecordList.add(inviteOneRecord);
//                    }
//                    /**投资*/
//                    if(null != distribution.getInvestRewardOne()){
//                        TradeRecordVo investOneRecord = new TradeRecordVo();
//                        investOneRecord.setType(BalanceStatementTypeEnum.REWARD.getCode());
//                        investOneRecord.setAmount(distribution.getInvestRewardOne());
//                        investOneRecord.setWay(FinanceWayEnum.IN.getCode());
//                        investOneRecord.setTelephone(RegexUtil.replaceTelephone(levelOneUserPhone));
//                        investOneRecord.setUserId(distribution.getUserId());
//                        investRecordList.add(investOneRecord);
//                    }
//                }
//                /**二级分销*/
//                String levelTwoUserPhone = userMapper.getUserNameByUserId(distribution.getDisLevelTwoUserId());
//                if(StringUtils.isNotEmpty(levelTwoUserPhone)){
//                    /**注册*/
//                    if(null != distribution.getInviteRewardTwo()){
//                        TradeRecordVo inviteTwoRecord = new TradeRecordVo();
//                        inviteTwoRecord.setType(BalanceStatementTypeEnum.REWARD.getCode());
//                        inviteTwoRecord.setAmount(distribution.getInviteRewardTwo());
//                        inviteTwoRecord.setWay(FinanceWayEnum.IN.getCode());
//                        inviteTwoRecord.setTelephone(RegexUtil.replaceTelephone(levelTwoUserPhone));
//                        inviteTwoRecord.setUserId(distribution.getUserId());
//                        inviteRecordList.add(inviteTwoRecord);
//                    }
//                    /**投资*/
//                    if(null != distribution.getInvestRewardTwo()){
//                        TradeRecordVo investTwoRecord = new TradeRecordVo();
//                        investTwoRecord.setType(BalanceStatementTypeEnum.REWARD.getCode());
//                        investTwoRecord.setAmount(distribution.getInvestRewardTwo());
//                        investTwoRecord.setWay(FinanceWayEnum.IN.getCode());
//                        investTwoRecord.setTelephone(RegexUtil.replaceTelephone(levelTwoUserPhone));
//                        investTwoRecord.setUserId(distribution.getUserId());
//                        investRecordList.add(investTwoRecord);
//                    }
//                }
//            }
//        }
//    }
}
