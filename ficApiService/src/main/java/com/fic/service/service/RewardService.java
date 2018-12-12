package com.fic.service.service;

import com.fic.service.entity.Invest;
import com.fic.service.entity.User;

/**
 *   @Author Xie
 *   @Date 2018/12/3
 *   @Discription:
**/
public interface RewardService {

    /**
     * 分销记录 ，注册，投资
     * @param user
     * @param inviteUser
     * @param action true 注册 false投资
     * @return
     */
     boolean distributionRewardByAction(User user, User inviteUser, Invest invest, boolean action,Integer investDetailId);
}
