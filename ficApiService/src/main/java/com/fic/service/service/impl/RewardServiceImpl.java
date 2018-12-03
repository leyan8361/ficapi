package com.fic.service.service.impl;

import com.fic.service.Enum.DistributionStatusEnum;
import com.fic.service.entity.*;
import com.fic.service.mapper.DistributionMapper;
import com.fic.service.mapper.InvestMapper;
import com.fic.service.mapper.RewardMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean distributionRewardByAction(User user, User inviteUser,boolean action) {

        Reward reward = rewardMapper.selectRulesByCurrentUserCount();

        if(null == reward){
            log.error("奖励规则 异常，请补充规则数据 ");
            throw new RuntimeException();
        }

        /**
         * 记录一级
         */
        Distribution distribution = distributionMapper.findByUserId(inviteUser.getId());
        int saveOneResult = 0 ;
        if(null == distribution){
            /** 无分销记录 */
            if(action){
                /**注册*/
                distribution = new Distribution();
                distribution.setUserId(inviteUser.getId());
                distribution.setInviteRewardOne(reward.getInviteRewardFirst());
                distribution.setCreatedTime(new Date());
                distribution.setDisLevelOneUserId(user.getId());
                distribution.setStatus(DistributionStatusEnum.FIRST_LEVEL.getCode());
            }else{
                /**投资*/
                log.error(" 投资无分销记录 异常 投资用户ID :{},上级用户ID:{}",user.getId(),inviteUser.getId());
                throw new RuntimeException();
            }
            saveOneResult = distributionMapper.insert(distribution);
        }else{
            if(action){
                /**注册*/
                distribution.setDisLevelOneUserId(user.getId());
                distribution.setInviteRewardOne(reward.getInviteRewardFirst());
            }else{
                /**投资*/
                distribution.setInvestRewardOne(reward.getInvestRewardFirst());
            }
            distribution.setUpdatedTime(new Date());
            saveOneResult = distributionMapper.updateByPrimaryKey(distribution);
        }

        if(saveOneResult <=0){
            log.error(" 记录一级分销 失败");
            throw new RuntimeException();
        }

        /**
         * 记录父级
         */
        User fatherUser = userMapper.findByInviteCode(inviteUser.getTuserInviteCode());
        if(null != fatherUser){
            int saveFatherResult = 0;
            Distribution distributionTwo = distributionMapper.findByUserId(fatherUser.getId());
            /** 父级余额变动 */
            Invest investFather = investMapper.findByUserId(fatherUser.getId());
            if(null == investFather){
                log.error(" father user invest not exist userID :{}", fatherUser.getId());
                throw new RuntimeException();
            }
            BalanceStatement balanceStatement = new BalanceStatement();
            balanceStatement.setUserId(fatherUser.getId());
            if(null == distributionTwo){
                /**父级无分销记录*/
                distributionTwo = new Distribution();
                distributionTwo.setUserId(fatherUser.getId());
                distributionTwo.setDisLevelOneUserId(inviteUser.getId());
                distributionTwo.setDisLevelTwoUserId(user.getId());
                distributionTwo.setStatus(DistributionStatusEnum.SECOND_LEVEL.getCode());
            }
            if(action){
                /**注册*/
                distributionTwo.setInviteRewardOne(reward.getInviteRewardFirst());
                distributionTwo.setInviteRewardTwo(reward.getInviteRewardSecond());
                balanceStatement.setAmount(reward.getInviteRewardSecond());
            }else{
                /**投资*/
                distributionTwo.setInvestRewardOne(reward.getInvestRewardFirst());
                distributionTwo.setInviteRewardTwo(reward.getInvestRewardSecond());
            }
            if(null == distributionTwo.getId()){
                distributionTwo.setUpdatedTime(new Date());
                saveFatherResult = distributionMapper.updateByPrimaryKey(distributionTwo);
            }else{
                distributionTwo.setCreatedTime(new Date());
                distributionTwo.setUpdatedTime(new Date());
                saveFatherResult = distributionMapper.insert(distributionTwo);
            }
            if(saveFatherResult <=0){
                log.error(" 记录父级分销 失败");
                throw new RuntimeException();
            }




        }


        /**
         * 当前用户分销
         */
        Distribution distributionSelf = new Distribution();
        distributionSelf.setUserId(user.getId());
        distributionSelf.setUpdatedTime(new Date());
        distributionSelf.setCreatedTime(new Date());
        distributionSelf.setStatus(DistributionStatusEnum.NO_DISTRBUTE.getCode());
        int saveSelf = distributionMapper.insert(distributionSelf);
        if(saveSelf <=0){
            log.error(" 记录自己分销 失败");
            throw new RuntimeException();
        }

        /**
         * 余额变动
         */


        return true;
    }
}
