package com.fic.service.service.impl;

import com.fic.service.Vo.InvestInfoVo;
import com.fic.service.Vo.InvestRecordInfoVo;
import com.fic.service.Vo.InvestRecordItemInfoVo;
import com.fic.service.Vo.InvestSuccessInfoVo;
import com.fic.service.controller.api.ApiInvestController;
import com.fic.service.entity.Invest;
import com.fic.service.entity.InvestDetail;
import com.fic.service.entity.Movie;
import com.fic.service.entity.User;
import com.fic.service.mapper.InvestDetailMapper;
import com.fic.service.mapper.InvestMapper;
import com.fic.service.mapper.MovieMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.InvestService;
import com.fic.service.service.RewardService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.SerialNumGenerateUtil;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.omg.SendingContext.RunTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:
**/
@Service
public class InvestServiceImpl implements InvestService {

    private final Logger log = LoggerFactory.getLogger(InvestServiceImpl.class);

    @Autowired
    MovieMapper movieMapper;
    @Autowired
    InvestDetailMapper investDetailMapper;
    @Autowired
    InvestMapper investMapper;
    @Autowired
    RewardService rewardService;
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public InvestSuccessInfoVo invest(Invest invest, InvestInfoVo investInfoVo) {

        Movie movie = movieMapper.selectByPrimaryKey(investInfoVo.getMoveId());
        boolean insert = false;
        if(null == movie){
            movie = new Movie();
            movie.setMovieId(investInfoVo.getMoveId());
            movie.setMovieName(investInfoVo.getMoveName());
            movie.setCreatedTime(new Date());
            insert = true;
        }
        movie.setUpdatedTime(new Date());
        int movieResult = 0;
        if(insert){
            movieResult = movieMapper.insert(movie);
        }else{
            movieResult = movieMapper.updateByPrimaryKey(movie);
        }
        if(movieResult <=0){
            log.error("投资--> Save Movie Failed");
            throw new RuntimeException();
        }

        InvestDetail investDetail = new InvestDetail();
        investDetail.setAmount(investInfoVo.getAmount());
        investDetail.setInTime(new Date());
        investDetail.setObTime(new Date());
        investDetail.setInvestId(invest.getInvestId());
        investDetail.setMovieId(investInfoVo.getMoveId());
        investDetail.setInvestDetailCode(SerialNumGenerateUtil.getSerialNumber());
        investDetail.setUserId(invest.getUserId());
        investDetail.setCreatedTime(new Date());
        investDetail.setUpdatedTime(new Date());
        int investDetailResult = investDetailMapper.insert(investDetail);
        if(investDetailResult <=0){
            log.error("投资--> Save Invest Detail Failed");
            throw new RuntimeException();
        }

        invest.setQty(invest.getQty() + 1);

        /**
         * 判断投资金额是否大于奖励金，若是则奖励金清零，启用余额减去投资剩余
         */
        BigDecimal balance = invest.getBalance();
        if(investInfoVo.getAmount().compareTo(invest.getRewardBalance()) >= 0){
            BigDecimal restAmount = investInfoVo.getAmount().subtract(invest.getRewardBalance());
            invest.setRewardBalance(BigDecimal.ZERO);
            balance = balance.subtract(restAmount);
            invest.setBalance(balance);
        }else{
            balance = invest.getRewardBalance().subtract(investInfoVo.getAmount());
            invest.setRewardBalance(balance);
        }

        invest.setUpdatedTime(new Date());

        int updateInvest = investMapper.updateByPrimaryKey(invest);
        if(updateInvest <=0){
            log.error("投资--> Save Invest Failed");
            throw new RuntimeException();
        }

        List<Integer> countInvestNum = investDetailMapper.countInvestPeople(movie.getMovieId());
        InvestSuccessInfoVo result = new InvestSuccessInfoVo();
        result.setRankingOfInvest(countInvestNum.size());

        /**
         * 判断是否存在分销
         */
        User user = userMapper.get(invest.getUserId());
        if(StringUtils.isNotEmpty(user.getTuserInviteCode())){
            User inviteUser = userMapper.findByInviteCode(user.getTuserInviteCode());
            boolean disResult = rewardService.distributionRewardByAction(user,inviteUser,invest,false);
            if(!disResult){
                log.error("分销失败，投资人ID:{}, 上级分销ID:{}",user.getId(),inviteUser.getId());
                throw new RuntimeException();
            }
        }

        return result;
    }

    @Override
    public InvestRecordInfoVo getInvestDetail(Integer userId) {
        InvestRecordInfoVo recordInfoVo = new InvestRecordInfoVo();
        List<InvestRecordItemInfoVo> list = new ArrayList<>();
        List<InvestDetail> details = investDetailMapper.findByUserId(userId);
        if(details.size() <= 0){
            return recordInfoVo;
        }

        Integer investId = 0;
        for(InvestDetail detail: details){
            investId = detail.getInvestId();
            Movie movie = movieMapper.selectByPrimaryKey(detail.getMovieId());
            if(null == movie)continue;
            InvestRecordItemInfoVo item = new InvestRecordItemInfoVo();
            item.setAmount(detail.getAmount());
            item.setCode(detail.getInvestDetailCode());
            item.setInTime(DateUtil.formatSec(detail.getInTime()));
            item.setMovieName(movie.getMovieName());
            item.setInvestDetailId(detail.getInvestDetailId());
            list.add(item);
        }

        recordInfoVo.setInvestId(investId);
        recordInfoVo.setUserId(userId);
        recordInfoVo.setItems(list);

        return recordInfoVo;
    }
}
