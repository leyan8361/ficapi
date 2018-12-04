package com.fic.service.service.impl;

import com.fic.service.Enum.DistributionTypeEnum;
import com.fic.service.Vo.DistributionVo;
import com.fic.service.entity.Distribution;
import com.fic.service.entity.User;
import com.fic.service.mapper.DistributionMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.DistributionService;
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
        List<DistributionVo> distributionVoList = new ArrayList<DistributionVo>();
//        for(Distribution distribution: distributionList){
//
//            boolean noNeedToPlay = true;
//            if(null != distribution.getDisLevelOneUserId()){
//                DistributionVo distributionVo = new DistributionVo();
//                User userInviteOne = userMapper.get(distribution.getDisLevelOneUserId());
//                if(null != userInviteOne){
//                    distributionVo.setUserId(userInviteOne.getId());
//                    distributionVo.setTelephone(this.replaceTelephone(userInviteOne.getUserName()));
//                }else{
//                    continue;
//                }
//                if(null != distribution.getInviteRewardOne()){
//                    noNeedToPlay = false;
//                    distributionVo.setAmount(distribution.getInviteRewardOne());
//                    distributionVo.setLevel(DistributionTypeEnum.LEVEL_ONE.getCode());
//                    distributionVo.setType(DistributionTypeEnum.TYPE_REGISTER.getCode());
//                }
//
//                if(null != distribution.getInvestRewardOne()){
//                    noNeedToPlay = false
//                    distributionVo.setAmount(distribution.getInvestRewardOne());
//                    distributionVo.setLevel(DistributionTypeEnum.LEVEL_ONE.getCode());
//                    distributionVo.setType(DistributionTypeEnum.TYPE_INVEST.getCode());
//                }
//            }
//
//            if(null != distribution.getDisLevelTwoUserId()){
//                DistributionVo distributionVo = new DistributionVo();
//                User userInviteTwo = userMapper.get(distribution.getDisLevelTwoUserId());
//                if(null != userInviteTwo){
//                    distributionVo.setUserId(userInviteTwo.getId());
//                    distributionVo.setTelephone(this.replaceTelephone(userInviteTwo.getUserName()));
//                }else{
//                    continue;
//                }
//                if(null != distribution.getInviteRewardTwo()){
//                    noNeedToPlay = false
//                    distributionVo.setAmount(distribution.getInviteRewardTwo());
//                    distributionVo.setLevel(DistributionTypeEnum.LEVEL_TWO.getCode());
//                    distributionVo.setType(DistributionTypeEnum.TYPE_REGISTER.getCode());
//                }
//            }

//        }
        return null;
    }

    public String replaceTelephone(String telephone){
        telephone = telephone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return telephone;
    };

}
