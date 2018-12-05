package com.fic.service.service.impl;

import com.fic.service.Enum.DistributionTypeEnum;
import com.fic.service.Vo.DistributionVo;
import com.fic.service.entity.Distribution;
import com.fic.service.entity.User;
import com.fic.service.mapper.DistributionMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.DistributionService;
import com.fic.service.utils.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistributionServiceImpl implements DistributionService {

    @Autowired
    DistributionMapper distributionMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<DistributionVo> getMyDistributionRecord(Integer userId) {
        List<Distribution> distributionList = distributionMapper.findAllByUserId(userId);
        List<DistributionVo> disResultList = new ArrayList<DistributionVo>();
        for(Distribution distribution: distributionList){

            /**
             * 根据1级分销人ID组织
             */
            if(null != distribution.getDisLevelOneUserId()){

                User levelOneUser = userMapper.get(distribution.getDisLevelOneUserId());
                if(null == levelOneUser){
                    continue;
                }
                /**1级注册*/
                if(null != distribution.getInviteRewardOne()){
                    DistributionVo disRegisterOne = new DistributionVo();
                    disRegisterOne.setUserId(levelOneUser.getId());
                    disRegisterOne.setTelephone(RegexUtil.replaceTelephone(levelOneUser.getUserName()));
                    disRegisterOne.setAmount(distribution.getInviteRewardOne());
                    disRegisterOne.setLevel(DistributionTypeEnum.LEVEL_ONE.getCode());
                    disRegisterOne.setType(DistributionTypeEnum.TYPE_REGISTER.getCode());
                    disResultList.add(disRegisterOne);
                }

                /**1级投资*/
                if(null != distribution.getInvestRewardOne()){
                    DistributionVo disInvestOne = new DistributionVo();
                    disInvestOne.setUserId(levelOneUser.getId());
                    disInvestOne.setTelephone(RegexUtil.replaceTelephone(levelOneUser.getUserName()));
                    disInvestOne.setAmount(distribution.getInvestRewardOne());
                    disInvestOne.setLevel(DistributionTypeEnum.LEVEL_ONE.getCode());
                    disInvestOne.setType(DistributionTypeEnum.TYPE_INVEST.getCode());
                    disResultList.add(disInvestOne);
                }
            }


            /**
             * 根据父级分销人ID组织
             */
            if(null != distribution.getDisLevelTwoUserId()){

                User userInviteTwo = userMapper.get(distribution.getDisLevelTwoUserId());
                if(null == userInviteTwo){
                    continue;
                }
                /**2级注册*/
                if(null != distribution.getInviteRewardTwo()){
                    DistributionVo disInviteTwo = new DistributionVo();
                    disInviteTwo.setUserId(userInviteTwo.getId());
                    disInviteTwo.setTelephone(RegexUtil.replaceTelephone(userInviteTwo.getUserName()));
                    disInviteTwo.setAmount(distribution.getInviteRewardTwo());
                    disInviteTwo.setLevel(DistributionTypeEnum.LEVEL_TWO.getCode());
                    disInviteTwo.setType(DistributionTypeEnum.TYPE_REGISTER.getCode());
                    disResultList.add(disInviteTwo);
                }
                /**2级投资*/
                if(null != distribution.getInvestRewardTwo()){
                    DistributionVo disInvestTwo = new DistributionVo();
                    disInvestTwo.setUserId(userInviteTwo.getId());
                    disInvestTwo.setTelephone(RegexUtil.replaceTelephone(userInviteTwo.getUserName()));
                    disInvestTwo.setAmount(distribution.getInviteRewardTwo());
                    disInvestTwo.setLevel(DistributionTypeEnum.LEVEL_TWO.getCode());
                    disInvestTwo.setType(DistributionTypeEnum.TYPE_INVEST.getCode());
                    disResultList.add(disInvestTwo);
                }
            }

        }
        return disResultList;
    }

}
