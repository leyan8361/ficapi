package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.FinanceTypeEnum;
import com.fic.service.Enum.FinanceWayEnum;
import com.fic.service.Vo.*;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.AdminLogService;
import com.fic.service.service.BalanceService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    InvestMapper investMapper;
    @Autowired
    AdminLogService adminLogService;

    @Override
    public TradeRecordInfoVo getTradeRecord(Integer userId) {
        log.debug(" getTradeRecord 获取 交易明细");
        TradeRecordInfoVo result = new TradeRecordInfoVo();
        List<TradeRecordVo> tradeRecordVoList = new ArrayList<>();
        BigDecimal totalReceive = BigDecimal.ZERO;
        BigDecimal totalPay = BigDecimal.ZERO;
        List<BalanceStatement> statementList = balanceStatementMapper.findAllByUserId(userId);
        if(statementList.size() <= 0)return result;
        for(BalanceStatement statement: statementList){
            if(FinanceTypeEnum.REWARD.getCode().equals(statement.getType())){
                String userPhone = userMapper.getUserNameByUserId(statement.getUserId());
                if(StringUtils.isEmpty(userPhone))continue;

                TradeRecordVo tradeRecordVo = new TradeRecordVo();
                tradeRecordVo.setUserId(statement.getUserId());
                tradeRecordVo.setTelephone(RegexUtil.replaceTelephone(userPhone));
                tradeRecordVo.setWay(statement.getWay());
                tradeRecordVo.setAmount(statement.getAmount());
                tradeRecordVo.setType(statement.getType());
                tradeRecordVo.setCreatedTime(DateUtil.formatMin(statement.getCreatedTime()));
                if(DateUtil.isToday(statement.getCreatedTime())){
                    if(statement.getWay().equals(FinanceWayEnum.IN)){
                        totalReceive = totalReceive.add(statement.getAmount());
                    }
                    if(statement.getWay().equals(FinanceWayEnum.OUT)){
                        totalPay = totalPay.add(statement.getAmount());
                    }
                }
                /**投资电影*/
                if(FinanceTypeEnum.INVEST.getCode().equals(statement.getType())){
                    String moveName = investDetailMapper.findMoveNameByDetailId(statement.getInvestDetailId());
                    tradeRecordVo.setMoveName(moveName);
                }
                /**分销 TODO 不需要处理 */
//                if(FinanceTypeEnum.REWARD.getCode().equals(statement.getType())){
//                    Distribution distribution = distributionMapper.selectByPrimaryKey(statement.getDistributionId());
//                }
                tradeRecordVoList.add(tradeRecordVo);
            }
        }

        result.setRecords(tradeRecordVoList);
        result.setTotalPay(totalPay);
        result.setTotalReceive(totalReceive);

        return result;
    }


    @Override
    public ResponseVo reward(RewardInfoVo rewardInfoVo) {
        User user = userMapper.findByUsername(rewardInfoVo.getUsername());
        if(null == user)return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        Invest invest = investMapper.findByUserId(user.getId());
        if(null == invest)return new ResponseVo(ErrorCodeEnum.INVEST_NOT_EXIST,null);

        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(user.getId());
        balanceStatement.setAmount(rewardInfoVo.getReward());
        balanceStatement.setCreatedTime(new Date());
        balanceStatement.setWay(FinanceWayEnum.IN.getCode());
        balanceStatement.setType(FinanceTypeEnum.REWARD.getCode());

        int saveBalanceRecord = balanceStatementMapper.insertSelective(balanceStatement);
        if(saveBalanceRecord <=0){
            log.error(" 运营 奖励后台 失败 用户ID:{}",user.getId());
            throw new RuntimeException();
        }
        int updateInvestResult = investMapper.updateRewardBalance(invest.getRewardBalance().add(rewardInfoVo.getReward()),user.getId());
        if(updateInvestResult <=0){
            log.error(" 运营 更新奖励余额失败:{}",user.getId());
            throw new RuntimeException();
        }

        adminLogService.saveAdminLog(new AdminLog("事件:奖励用户"+rewardInfoVo.getReward()+"；用户ID:"+user.getId(),new Date()));
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public List<RewardRecordInfoVo> getReward(Integer userId) {
        List<RewardRecordInfoVo> tradeRecordVoList = new ArrayList<>();
        User user = userMapper.get(userId);
        if(null == user){
            log.error(" 运营 获取用户奖励记录失败，用户ID:{}",user.getId());
            throw new RuntimeException();
        }
        List<BalanceStatement> statementList = balanceStatementMapper.findAllByUserId(userId);
        if(statementList.size() <= 0)return tradeRecordVoList;
        for(BalanceStatement statement: statementList){
            if(FinanceTypeEnum.REWARD.getCode().equals(statement.getType())){
                String userPhone = userMapper.getUserNameByUserId(statement.getUserId());
                if(StringUtils.isEmpty(userPhone))continue;

                RewardRecordInfoVo tradeRecordVo = new RewardRecordInfoVo();
                tradeRecordVo.setUserId(statement.getUserId());
                tradeRecordVo.setTelephone(RegexUtil.replaceTelephone(userPhone));
                tradeRecordVo.setWay(statement.getWay());
                tradeRecordVo.setAmount(statement.getAmount());
                tradeRecordVo.setType(statement.getType());
                tradeRecordVo.setCreatedTime(DateUtil.formatMin(statement.getCreatedTime()));
                tradeRecordVo.setDistributionId(statement.getDistributionId());
                tradeRecordVo.setInvestDetailId(statement.getInvestDetailId());
                tradeRecordVoList.add(tradeRecordVo);
            }
        }
        return tradeRecordVoList;
    }

}
