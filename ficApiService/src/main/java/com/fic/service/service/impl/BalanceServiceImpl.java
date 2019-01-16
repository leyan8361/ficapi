package com.fic.service.service.impl;

import com.fic.service.Enum.*;
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
import java.util.*;

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
    @Autowired
    BetUserMapper betUserMapper;
    @Autowired
    RewardMapper rewardMapper;

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


    @Override
    public ResponseVo getTradeRecordV2(TradeRecordRequestVo condition) {
        TradeRecordInfoV2Vo result = new TradeRecordInfoV2Vo();
        String startDay = DateUtil.getThisMonthBegin(condition.getMonth());
        String endDay = DateUtil.getThisMonthEnd(condition.getMonth());
        int offset = condition.getPageNum()*10;
        List<BalanceStatement> findResult = balanceStatementMapper.findByCondition(startDay,endDay,condition,offset);
        if(findResult.size() == 0){
            return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
        }
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpend = BigDecimal.ZERO;
        List<TradeRecordV2Vo> items = new ArrayList<TradeRecordV2Vo>();
        StringBuilder betBingoIds = new StringBuilder();
        StringBuilder betReturningIds = new StringBuilder();
        betBingoIds.append("0");
        betReturningIds.append("0");
        Reward reward = rewardMapper.selectRulesByCurrentUserCount();
        for(BalanceStatement balanceStatement: findResult){
            BigDecimal amount = balanceStatement.getAmount();
            TradeRecordV2Vo item = new TradeRecordV2Vo();
            /** 分销奖励 */
            if(balanceStatement.getType() == FinanceTypeEnum.REWARD.getCode()){
                Distribution distribution = distributionMapper.selectByPrimaryKey(balanceStatement.getDistributionId());
                if(null == distribution) {
                    log.error(" 查询交易明细失败，分销奖励结果为空 distribution id :{}", balanceStatement.getDistributionId());
                    continue;
                }

                if(amount.compareTo(reward.getInviteRewardFirst()) == 0){
                    /** 300 判断是一级注册 or 二级投资 时间前的是注册*/
                    List<BalanceStatement> repeatReward = balanceStatementMapper.findAllSameAmountWithUserDis(amount,balanceStatement.getUserId(),distribution.getId(),balanceStatement.getId());
                    if(repeatReward.size() != 0){
                        for(BalanceStatement repeatAmountBalance : repeatReward){
                            if(balanceStatement.getCreatedTime().compareTo(repeatAmountBalance.getCreatedTime()) < 0){
                                /**一级注册*/
                                item.setDistributionType(TradeRecordDistributionEnum.INVITE_ONE.getCode());
                                break;
                            }else{
                                /**二级投资*/
                                item.setDistributionType(TradeRecordDistributionEnum.INVEST_TWO.getCode());
                                break;
                            }
                        }
                    }else{
                        /** 只有一条 300 必定是一级注册*/
                        item.setDistributionType(TradeRecordDistributionEnum.INVITE_ONE.getCode());
                    }
                    item.setCreatedTime(balanceStatement.getCreatedTime());
                }
                if(amount.compareTo(reward.getInvestRewardFirst()) == 0){
                    /** 1000 判断是自己注册 or 一级投资 时间前的是注册*/
                    if(null == balanceStatement.getInvestDetailId()){
                        /** 没有投资ID，一定是注册*/
                        item.setDistributionType(TradeRecordDistributionEnum.REGISTER.getCode());
                    }else{
                        /**一级投资*/
                        item.setDistributionType(TradeRecordDistributionEnum.INVEST_ONE.getCode());
                    }
//                    List<BalanceStatement> repeatReward = balanceStatementMapper.findAllSameAmountWithUserDis(amount,balanceStatement.getUserId(),distribution.getId(),balanceStatement.getId());
//                    if(repeatReward.size() != 0){
//                        for(BalanceStatement repeatAmountBalance : repeatReward){
//                            if(balanceStatement.getCreatedTime().compareTo(repeatAmountBalance.getCreatedTime()) < 0){
//                                /**自己注册*/
//                                item.setDistributionType(TradeRecordDistributionEnum.REGISTER.getCode());
//                                break;
//                            }else{
//                                /**一级投资*/
//                                item.setDistributionType(TradeRecordDistributionEnum.INVEST_ONE.getCode());
//                                break;
//                            }
//                        }
//                    }else{
//                        /** 只有一条 1000 必定是自己注册*/
//                        item.setDistributionType(TradeRecordDistributionEnum.REGISTER.getCode());
//                    }
                    item.setCreatedTime(balanceStatement.getCreatedTime());
                }

                /** 100 */
                if(amount.compareTo(reward.getInviteRewardSecond()) == 0){
                    item.setDistributionType(TradeRecordDistributionEnum.INVITE_TWO.getCode());
                }

                if(null == item.getDistributionType()){
                    log.error(" 查询交易明细 无对应分销记录, balance id :{}, distribution id :{}",balanceStatement.getId(),distribution.getId());
                    continue;
                }
                item.setAmount(amount);
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }

            /** 投资 */
            if(balanceStatement.getType() == FinanceTypeEnum.INVEST.getCode()){
                InvestDetail detail =  investDetailMapper.selectByPrimaryKey(balanceStatement.getInvestDetailId());
                if(null == detail){
                    log.error("查询交易明细失败，投资记录，invest detail 为空， id :{}",balanceStatement.getInvestDetailId());
                    continue;
                }
                Movie movie = movieMapper.selectByPrimaryKey(detail.getMovieId());
                if(null == movie){
                    log.error("查询交易明细失败，投资电影为空, movie Id :{},invest detail id :{}",detail.getMovieId(),detail.getInvestDetailId());
                    continue;
                }
                item.setMoveName(movie.getMovieName());
                item.setAmount(detail.getAmount());
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }

            /** 竞猜奖励 */
            if(balanceStatement.getType() == FinanceTypeEnum.BET_REWARD.getCode()){
                List<BetUser> betUsers = betUserMapper.findByBingoPriceAndUserId(balanceStatement.getAmount(),balanceStatement.getUserId(),startDay,endDay,betBingoIds.toString().split(","));
                if(betUsers.size() == 0){
                    log.error(" 查询交易明细失败，竞猜记录异常，无相差竞猜记录 bet amount :{},userId :{},startDay :{},endDay:{},ids:{}",balanceStatement.getAmount(),balanceStatement.getUserId(),startDay,endDay,betBingoIds.toString());
                    continue;
                }
                String movieName = betUserMapper.findMovieNameById(betUsers.get(0).getId());
                if(StringUtils.isEmpty(movieName)){
                    log.error("查询交易明细失败，竞猜记录异常，无相关电影 bet scence movie id :{}",betUsers.get(0).getBetScenceMovieId());
                    continue;
                }
                betBingoIds.append(",").append(betUsers.get(0).getId());
                item.setMoveName(movieName);
                item.setAmount(balanceStatement.getAmount());
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }

            /**竞猜返还*/
            if(balanceStatement.getType() == FinanceTypeEnum.BET_RETURNING.getCode()){
                List<BetUser> betUsers = betUserMapper.findByReturningAndUserId(balanceStatement.getAmount(),balanceStatement.getUserId(),startDay,endDay,betReturningIds.toString().split(","));
                if(betUsers.size() == 0){
                    log.error(" 查询交易明细失败，竞猜记录异常，无相关竞猜返还记录 bet amount :{},userId :{},startDay :{},endDay:{},ids:{}",balanceStatement.getAmount(),balanceStatement.getUserId(),startDay,endDay,betReturningIds.toString());
                    continue;
                }
                String movieName = betUserMapper.findMovieNameById(betUsers.get(0).getId());
                if(StringUtils.isEmpty(movieName)){
                    log.error("查询交易明细失败，竞猜记录异常，无相关电影 bet scence movie id :{}",betUsers.get(0).getBetScenceMovieId());
                    continue;
                }
                betReturningIds.append(",").append(betUsers.get(0).getId());
                item.setMoveName(movieName);
                item.setAmount(balanceStatement.getAmount());
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }

            /**投注*/
            if(balanceStatement.getType() == FinanceTypeEnum.BET.getCode()){
                BetUser betUser = betUserMapper.findByBetAmountAndUserIdAndCreatedTime(balanceStatement.getAmount(),balanceStatement.getUserId(),DateUtil.dateToStrMatSec(balanceStatement.getCreatedTime()));
                if(null == betUser){
                    log.error(" 查询交易明细失败，竞猜记录异常，无相关竞猜投注记录 bet amount :{},userId :{},createdTime :{}",balanceStatement.getAmount(),balanceStatement.getUserId(),DateUtil.dateToStrMatSec(balanceStatement.getCreatedTime()));
                    continue;
                }
                String movieName = betUserMapper.findMovieNameById(betUser.getId());
                if(StringUtils.isEmpty(movieName)){
                    log.error("查询交易明细失败，竞猜记录异常，无相关电影 bet scence movie id :{}",betUser.getBetScenceMovieId());
                    continue;
                }
                item.setMoveName(movieName);
                item.setAmount(balanceStatement.getAmount());
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }

            /** 连续奖励 */
            if(balanceStatement.getType() == FinanceTypeEnum.BET_REWARD_POOL.getCode()){
                item.setAmount(balanceStatement.getAmount().setScale(0,BigDecimal.ROUND_DOWN));
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }


            /** 转出 */
            if(balanceStatement.getType() == FinanceTypeEnum.TRANSFER_OUT.getCode()){
                item.setAmount(balanceStatement.getAmount().setScale(0,BigDecimal.ROUND_DOWN));
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }

            /** 转入 */
            if(balanceStatement.getType() == FinanceTypeEnum.PAYEE_IN.getCode()){
                item.setAmount(balanceStatement.getAmount().setScale(0,BigDecimal.ROUND_DOWN));
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }

            /** 充值 */
            if(balanceStatement.getType() == FinanceTypeEnum.RECHARGE.getCode()){
                item.setAmount(balanceStatement.getAmount().setScale(0,BigDecimal.ROUND_DOWN));
                item.setCreatedTime(balanceStatement.getCreatedTime());
            }

            item.setType(balanceStatement.getType());
            item.setWay(balanceStatement.getWay());

            if(balanceStatement.getWay() == FinanceWayEnum.IN.getCode()){
                /** 收入 */
                totalIncome = totalIncome.add(item.getAmount());
            }
            if(balanceStatement.getWay() == FinanceWayEnum.OUT.getCode()){
                /** 支出 */
                totalExpend = totalExpend.add(item.getAmount());
            }

            items.add(item);
        }

        result.setItems(items);
        result.setIncome(totalIncome);
        result.setExpend(totalExpend);

        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }
}
