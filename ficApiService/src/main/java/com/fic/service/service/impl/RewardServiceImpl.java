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
    public boolean distributionRewardByAction(User user, User inviteUser,Invest invest,boolean action) {

        Reward reward = rewardMapper.selectRulesByCurrentUserCount();
        if(null == reward){
            log.error("奖励规则 异常，请补充规则数据 ");
            throw new RuntimeException();
        }


        /**
         * 记录二级
         */
        Distribution distribution = distributionMapper.findByUserId(inviteUser.getId(),user.getId(),action);
        Invest investInviteUser = investMapper.findByUserId(inviteUser.getId());
        int saveOneResult = 0 ;
        boolean alreadyDisTwo = false;
        if(null == distribution){
            /** 无分销记录 */
            distribution = new Distribution();
            distribution.setUserId(inviteUser.getId());
            distribution.setCreatedTime(new Date());
            distribution.setUpdatedTime(new Date());
            if(action){
                /**注册*/
                distribution.setInviteRewardOne(reward.getInviteRewardFirst());
            }else{
                /**投资*/
                distribution.setInvestRewardOne(reward.getInvestRewardFirst());
            }
            distribution.setDisLevelOneUserId(user.getId());
            distribution.setStatus(DistributionStatusEnum.REGISTER_FIRST_LEVEL.getCode());
            distribution.setInvestStatus(DistributionStatusEnum.INVEST_NO_DISTRBUTE.getCode());
            saveOneResult = distributionMapper.insert(distribution);
            if(saveOneResult <=0){
                log.error(" 记录二级分销 失败");
                throw new RuntimeException();
            }
        }else{
            if(action){
                /**注册*/
                if(null == distribution.getInviteRewardTwo()){
                    distribution.setStatus(DistributionStatusEnum.REGISTER_FIRST_LEVEL.getCode());
                }else{
                    distribution.setStatus(DistributionStatusEnum.REGISTER_SECOND_LEVEL.getCode());
                }
                if(null != distribution.getInviteRewardOne()){
                    alreadyDisTwo = true;
                }else{
                    distribution.setDisLevelOneUserId(user.getId());
                    distribution.setInviteRewardOne(reward.getInviteRewardFirst());
                }
            }else{
                /**投资*/
                if(null == distribution.getInvestRewardTwo()){
                    distribution.setInvestStatus(DistributionStatusEnum.INVEST_FIRST_LEVEL.getCode());
                }else{
                    distribution.setInvestStatus(DistributionStatusEnum.INVEST_SECOND_LEVEL.getCode());
                }
                if(null != distribution.getInvestRewardOne()){
                    alreadyDisTwo = true;
                }else{
                    distribution.setDisLevelOneUserId(user.getId());
                    distribution.setInvestRewardOne(reward.getInvestRewardFirst());
                }
            }
            if(!alreadyDisTwo){
                distribution.setUpdatedTime(new Date());
                saveOneResult = distributionMapper.updateByPrimaryKey(distribution);
                if(saveOneResult <=0){
                    log.error(" 记录二级分销 失败");
                    throw new RuntimeException();
                }
            }

        }

        if(!alreadyDisTwo){
            /**
             * 二级余额变动
             */
            BigDecimal calBalanceInviteUser = null;
            BalanceStatement balanceStatementInviteUser = new BalanceStatement();
            if(action){
                /**注册*/
                calBalanceInviteUser = investInviteUser.getRewardBalance().add(reward.getInviteRewardFirst());
                balanceStatementInviteUser.setAmount(reward.getInviteRewardFirst());
            }else{
                /**投资*/
                calBalanceInviteUser = investInviteUser.getRewardBalance().add(reward.getInvestRewardFirst());
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
                boolean alreadyReward = false;

                if(action){
                    /**注册*/
                    if(null != distributionTwo.getInviteRewardOne()){
                        distributionTwo.setStatus(DistributionStatusEnum.REGISTER_SECOND_LEVEL.getCode());
                    }else{
                        distributionTwo.setStatus(DistributionStatusEnum.REGISTER_FIRST_LEVEL.getCode());
                    }
                    /**判断是否已奖励过*/
                    if(null != distributionTwo.getInviteRewardTwo()){
                        alreadyReward = true;
                    }else{
                        distributionTwo.setInviteRewardTwo(reward.getInviteRewardSecond());
                    }
                }else{
                    /**投资*/
                    if(null != distributionTwo.getInvestRewardOne()){
                        distributionTwo.setInvestStatus(DistributionStatusEnum.INVEST_SECOND_LEVEL.getCode());
                    }else{
                        distributionTwo.setInvestStatus(DistributionStatusEnum.INVEST_FIRST_LEVEL.getCode());
                    }
                    /**判断是否已奖励过*/
                    if(null != distributionTwo.getInvestRewardTwo()){
                        alreadyReward = true;
                    }else{
                        distributionTwo.setInvestRewardTwo(reward.getInvestRewardSecond());
                    }
                }
                if(null == distributionTwo.getId()){
                    distributionTwo.setUpdatedTime(new Date());
                    distributionTwo.setCreatedTime(new Date());
                    saveFatherResult = distributionMapper.insert(distributionTwo);
                }else if( null != distributionTwo.getId() && !alreadyReward){
                    distributionTwo.setUpdatedTime(new Date());
                    saveFatherResult = distributionMapper.updateByPrimaryKey(distributionTwo);
                }else{
                    saveFatherResult = 1;
                }
                if(saveFatherResult <=0){
                    log.error(" 记录父级分销 失败");
                    throw new RuntimeException();
                }
                if(!alreadyReward){
                    /** 父级余额变动 */
                    BigDecimal calBalance = null;
                    BalanceStatement balanceStatement = new BalanceStatement();
                    if(action){
                        /**注册*/
                        calBalance = investFather.getRewardBalance().add(reward.getInviteRewardSecond());
                        balanceStatement.setAmount(reward.getInviteRewardSecond());
                    }else{
                        /**投资*/
                        calBalance = investFather.getRewardBalance().add(reward.getInvestRewardSecond());
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

            }else{
                //产生新三级分销数据
                /**父级无分销记录*/
                distributionTwo = new Distribution();
                distributionTwo.setUserId(fatherUser.getId());
                distributionTwo.setDisLevelOneUserId(inviteUser.getId());
                distributionTwo.setDisLevelTwoUserId(user.getId());
                distributionTwo.setUpdatedTime(new Date());
                distributionTwo.setCreatedTime(new Date());
                if(action){
                    /**注册*/
                    distributionTwo.setInviteRewardTwo(reward.getInviteRewardSecond());
                    distributionTwo.setInviteRewardOne(reward.getInviteRewardFirst());
                    distributionTwo.setStatus(DistributionStatusEnum.REGISTER_SECOND_LEVEL.getCode());
                }else{
                    /**投资*/
                    distributionTwo.setInvestRewardTwo(reward.getInvestRewardSecond());
                    distributionTwo.setInvestRewardOne(reward.getInvestRewardFirst());
                    distributionTwo.setInvestStatus(DistributionStatusEnum.INVEST_SECOND_LEVEL.getCode());
                }
                saveFatherResult = distributionMapper.insert(distributionTwo);
                if(saveFatherResult <=0){
                    log.error(" 记录父级分销 失败");
                    throw new RuntimeException();
                }
                /** 父级余额变动 */
                BigDecimal calBalance = null;
                BalanceStatement balanceStatement = new BalanceStatement();
                if(action){
                    /**注册*/
                    calBalance = investFather.getRewardBalance().add(reward.getInviteRewardSecond());
                    balanceStatement.setAmount(reward.getInviteRewardSecond());
                }else{
                    /**投资*/
                    calBalance = investFather.getRewardBalance().add(reward.getInvestRewardSecond());
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
        return true;
    }
}
