package com.fic.service.service.impl;

import com.fic.service.Enum.DistributionStatusEnum;
import com.fic.service.Enum.FinanceTypeEnum;
import com.fic.service.Enum.FinanceWayEnum;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/12/3
 *   @Discription:
**/
@Service
public class RewardServiceImpl implements RewardService {

    private final Logger log = LoggerFactory.getLogger(RewardServiceImpl.class);

    @Autowired
    RewardMapper rewardMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    DistributionMapper distributionMapper;
    @Autowired
    InvestMapper investMapper;
    @Autowired
    BalanceStatementMapper balanceStatementMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean distributionRewardByAction(User user, User inviteUser,boolean action) {

        Reward reward = rewardMapper.selectRulesByCurrentUserCount();
        if(null == reward){
            log.error("奖励规则 异常，请补充规则数据 ");
            throw new RuntimeException();
        }

        Invest invest = null;
        if(action){
            /**
             * 生成默认资产记录
             */
            invest = new Invest();
            invest.setBalance(BigDecimal.ZERO);
            invest.setRewardBalance(BigDecimal.ZERO.add(reward.getRegisterSelf()));
            invest.setQty(0);
            invest.setUserId(user.getId());
            invest.setUpdatedTime(new Date());
            invest.setCreatedTime(new Date());
            int investResult = investMapper.insert(invest);
            if(investResult <=0){
                log.error("生成Invest 失败 ");
                throw new RuntimeException();
            }
        }else{
            invest = investMapper.findByUserId(user.getId());
        }


        /**
         * 记录二级
         */
        Distribution distribution = distributionMapper.findByUserId(inviteUser.getId(),action);
        Invest investInviteUser = investMapper.findByUserId(inviteUser.getId());
        int saveOneResult = 0 ;
        boolean doDistributionTwo = false;
        if(null == distribution){
            doDistributionTwo = true;
            /** 无分销记录 */
            distribution = new Distribution();
            distribution.setUserId(inviteUser.getId());
            distribution.setCreatedTime(new Date());
            distribution.setUpdatedTime(new Date());
            if(action){
                /**注册*/
                distribution.setInviteRewardOne(reward.getInviteRewardFirst());
                distribution.setDisLevelOneUserId(user.getId());
                distribution.setStatus(DistributionStatusEnum.REGISTER_FIRST_LEVEL.getCode());
                distribution.setInvestStatus(DistributionStatusEnum.INVEST_NO_DISTRBUTE.getCode());
            }else{
                /**投资*/
                distribution.setInvestRewardOne(reward.getInvestRewardFirst());
                distribution.setDisLevelOneUserId(user.getId());
                distribution.setStatus(DistributionStatusEnum.REGISTER_NO_DISTRBUTE.getCode());
                distribution.setInvestStatus(DistributionStatusEnum.INVEST_FIRST_LEVEL.getCode());
            }
            saveOneResult = distributionMapper.insert(distribution);
            if(saveOneResult <=0){
                log.error(" 记录二级分销 失败");
                throw new RuntimeException();
            }
        }else{
            doDistributionTwo = true;
            if(action){
                /**注册*/
                if(null == distribution.getInviteRewardTwo()){
                    distribution.setStatus(DistributionStatusEnum.REGISTER_FIRST_LEVEL.getCode());
                }else{
                    distribution.setStatus(DistributionStatusEnum.REGISTER_SECOND_LEVEL.getCode());
                }
                distribution.setDisLevelOneUserId(user.getId());
                distribution.setInviteRewardOne(reward.getInviteRewardFirst());
            }else{
                /**投资*/
                if(null == distribution.getInvestRewardTwo()){
                    distribution.setInvestStatus(DistributionStatusEnum.INVEST_FIRST_LEVEL.getCode());
                }else{
                    distribution.setInvestStatus(DistributionStatusEnum.INVEST_SECOND_LEVEL.getCode());
                }
                distribution.setInvestRewardOne(reward.getInvestRewardFirst());
            }
            distribution.setUpdatedTime(new Date());
            saveOneResult = distributionMapper.updateByPrimaryKey(distribution);
            if(saveOneResult <=0){
                log.error(" 记录二级分销 失败");
                throw new RuntimeException();
            }
        }

        if(doDistributionTwo){
            /**
             * 二级余额变动
             */
            BigDecimal calBalanceInviteUser = null;
            BalanceStatement balanceStatementInviteUser = new BalanceStatement();
            if(action){
                /**注册*/
                calBalanceInviteUser = (null!=investInviteUser.getBalance()?investInviteUser.getBalance(): BigDecimal.ZERO)
                        .add(null != investInviteUser.getRewardBalance()? investInviteUser.getRewardBalance():BigDecimal.ZERO)
                        .add(reward.getInviteRewardFirst());
                balanceStatementInviteUser.setAmount(reward.getInviteRewardFirst());
            }else{
                /**投资*/
                calBalanceInviteUser = (null!=investInviteUser.getBalance()?investInviteUser.getBalance(): BigDecimal.ZERO)
                        .add(null != investInviteUser.getRewardBalance()? investInviteUser.getRewardBalance():BigDecimal.ZERO)
                        .add(reward.getInvestRewardFirst());
                balanceStatementInviteUser.setAmount(reward.getInvestRewardFirst());
            }
            balanceStatementInviteUser.setUserId(inviteUser.getId());
            balanceStatementInviteUser.setDistributionId(distribution.getId());
            balanceStatementInviteUser.setBalance(calBalanceInviteUser);
            balanceStatementInviteUser.setCreatedTime(new Date());
            balanceStatementInviteUser.setType(FinanceTypeEnum.REWARD.getCode());
            balanceStatementInviteUser.setWay(FinanceWayEnum.IN.getCode());

            int saveBalanceResult = balanceStatementMapper.insert(balanceStatementInviteUser);
            int updateBalance = investMapper.updateRewardBalance(calBalanceInviteUser,inviteUser.getId());
            if(saveBalanceResult <=0 || updateBalance <=0)throw new RuntimeException("修改二级余额失败，或生成二级余额变动失败");
        }




        /**
         * 记录父级
         */
        User fatherUser = userMapper.findByInviteCode(inviteUser.getTuserInviteCode());
        if(null != fatherUser){
            int saveFatherResult = 0;
            Distribution distributionTwo = distributionMapper.findByFatherUserId(fatherUser.getId(),inviteUser.getId(),action);
            Invest investFather = investMapper.findByUserId(fatherUser.getId());
            if(null == investFather){
                log.error(" father user invest not exist userID :{}", fatherUser.getId());
                throw new RuntimeException();
            }
            if(null != distributionTwo){
                /**父级无分销记录*/
//                distributionTwo = new Distribution();
//                distributionTwo.setUserId(fatherUser.getId());
//                distributionTwo.setDisLevelOneUserId(inviteUser.getId());
                distributionTwo.setDisLevelTwoUserId(user.getId());
                if(action){
                    /**注册*/
                    if(null != distributionTwo.getInviteRewardOne()){
                        distributionTwo.setStatus(DistributionStatusEnum.REGISTER_SECOND_LEVEL.getCode());
                    }else{
                        distributionTwo.setStatus(DistributionStatusEnum.REGISTER_FIRST_LEVEL.getCode());
                    }
                    distributionTwo.setInviteRewardTwo(reward.getInviteRewardSecond());
                }else{
                    /**投资*/
                    if(null != distributionTwo.getInvestRewardOne()){
                        distributionTwo.setInvestStatus(DistributionStatusEnum.INVEST_SECOND_LEVEL.getCode());
                    }else{
                        distributionTwo.setInvestStatus(DistributionStatusEnum.INVEST_FIRST_LEVEL.getCode());
                    }

                    distributionTwo.setInvestRewardTwo(reward.getInvestRewardSecond());
                }
                if(null == distributionTwo.getId()){
                    distributionTwo.setUpdatedTime(new Date());
                    distributionTwo.setCreatedTime(new Date());
                    saveFatherResult = distributionMapper.insert(distributionTwo);
                }else{
                    distributionTwo.setUpdatedTime(new Date());
                    saveFatherResult = distributionMapper.updateByPrimaryKey(distributionTwo);
                }
                if(saveFatherResult <=0){
                    log.error(" 记录父级分销 失败");
                    throw new RuntimeException();
                }
                /** 父级余额变动 */
                BigDecimal calBalance = null;
                BalanceStatement balanceStatement = new BalanceStatement();
                if(action){
                    /**注册*/
                    calBalance = (null!=investFather.getBalance()?investFather.getBalance(): BigDecimal.ZERO).add(reward.getInviteRewardSecond());
                    balanceStatement.setAmount(reward.getInviteRewardSecond());
                }else{
                    /**投资*/
                    calBalance = (null!=investFather.getBalance()?investFather.getBalance(): BigDecimal.ZERO).add(reward.getInvestRewardSecond());
                    balanceStatement.setAmount(reward.getInvestRewardSecond());
                }
                balanceStatement.setUserId(fatherUser.getId());
                balanceStatement.setDistributionId(distributionTwo.getId());
                balanceStatement.setBalance(calBalance);
                balanceStatement.setCreatedTime(new Date());
                balanceStatement.setType(FinanceTypeEnum.REWARD.getCode());
                balanceStatement.setWay(FinanceWayEnum.IN.getCode());

                int saveFatherBalanceResult = balanceStatementMapper.insert(balanceStatement);
                investFather.setBalance(calBalance);
                int updateFatherBalance = investMapper.updateRewardBalance(calBalance,investFather.getUserId());
                if(saveFatherBalanceResult <=0 || updateFatherBalance <=0)throw new RuntimeException("修改父级余额失败，或生成父级余额变动失败");
            }


        }

        /**
         * 自己投资，无处理
         */
        if(action){
            /**
             * 当前用户分销
             */
            Distribution distributionSelf = new Distribution();
            distributionSelf.setUserId(user.getId());
            distributionSelf.setUpdatedTime(new Date());
            distributionSelf.setCreatedTime(new Date());
            distributionSelf.setStatus(DistributionStatusEnum.REGISTER_NO_DISTRBUTE.getCode());
            distributionSelf.setInvestStatus(DistributionStatusEnum.INVEST_NO_DISTRBUTE.getCode());
            int saveSelf = distributionMapper.insert(distributionSelf);
            if(saveSelf <=0){
                log.error(" 记录自己分销 失败");
                throw new RuntimeException();
            }
            /**
             * 余额变动
             */
            BigDecimal calBalanceSelf = reward.getRegisterSelf();
            BalanceStatement balanceStatementSelf = new BalanceStatement();
            /**注册*/
            balanceStatementSelf.setAmount(reward.getRegisterSelf());
            balanceStatementSelf.setUserId(user.getId());
            balanceStatementSelf.setAmount(reward.getRegisterSelf());
            balanceStatementSelf.setDistributionId(distributionSelf.getId());
            balanceStatementSelf.setBalance(calBalanceSelf);
            balanceStatementSelf.setCreatedTime(new Date());
            balanceStatementSelf.setType(FinanceTypeEnum.REWARD.getCode());
            balanceStatementSelf.setWay(FinanceWayEnum.IN.getCode());

            int saveBalanceSelf = balanceStatementMapper.insert(balanceStatementSelf);
            int updateBalanceSelf = investMapper.updateRewardBalance(calBalanceSelf,user.getId());
            if(saveBalanceSelf <=0 || updateBalanceSelf <=0)throw new RuntimeException("修改Self余额失败，或生成Self余额变动失败");
        }



        return true;
    }
}
