package com.fic.service.service.impl;

import com.fic.service.Enum.BalanceStatementTypeEnum;
import com.fic.service.Enum.DistributionStatusEnum;
import com.fic.service.Enum.DistributionTypeEnum;
import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.*;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.controller.HomeController;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.AccountService;
import com.fic.service.service.RewardService;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/11/26
 *   @Discription:
**/
@Service
public class AccountServiceImpl implements AccountService {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);


    private SecureRandom random = new SecureRandom();
    @Autowired
    TokenBaseMapper tokenBaseMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    InvestMapper investMapper;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    UploadProperties uploadProperties;
    @Autowired
    RewardMapper rewardMapper;
    @Autowired
    RewardService rewardService;
    @Autowired
    BalanceStatementMapper balanceStatementMapper;
    @Autowired
    DistributionMapper distributionMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public LoginUserInfoVo login(HttpServletRequest request,User user) {
        LoginUserInfoVo loginUserInfoVo = new LoginUserInfoVo();
        loginUserInfoVo.setUserId(user.getId());
        loginUserInfoVo.setHimageUrl(uploadProperties.getUrl(user.getHimageUrl()));
        loginUserInfoVo.setMyInviteCode(user.getUserInviteCode());
        loginUserInfoVo.setUsername(user.getUserName());

        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        TokenBase token = this.saveToken(user,userAgent,ipAddress);
        loginUserInfoVo.setTokenValue(token.getTokenValue());
        return loginUserInfoVo;
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public User register(RegisterUserInfoVo userInfoVo) {

        Reward reward = rewardMapper.selectRulesByCurrentUserCount();
        if(null == reward){
            log.error("奖励规则 异常，请补充规则数据 ");
            throw new RuntimeException();
        }

        User user = new User();
        user.setUserName(userInfoVo.getUsername());
        user.setPassword(userInfoVo.getPassword());
        user.setTuserInviteCode(userInfoVo.getInviteCode());
        user.setUserInviteCode(InviteCodeUtil.toSerialCode(userMapper.getLastInsertID()));
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        int result = userMapper.insert(user);

        if(result  <= 0){
            return null;
        }

        Invest invest = new Invest();
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



       if(StringUtils.isNotEmpty(userInfoVo.getInviteCode())){
           /**
            * 分销记录
            */
           User inviteByWho = userMapper.findByInviteCode(userInfoVo.getInviteCode());
           if(null == inviteByWho){
               log.error(" 注册用户 ---> 邀请码不存在 : {}",userInfoVo.getInviteCode());
               throw new RuntimeException();
           }

           boolean disResult = rewardService.distributionRewardByAction(user,inviteByWho,invest,true);
           if(!disResult){
               log.error(" 注册失败 --- > ");
               throw new RuntimeException();
           }
       }else{

           Distribution distribution = new Distribution();
           distribution.setUserId(user.getId());
           distribution.setCreatedTime(new Date());
           distribution.setUpdatedTime(new Date());
           distribution.setStatus(DistributionStatusEnum.REGISTER_NO_DISTRBUTE.getCode());
           distribution.setInvestStatus(DistributionStatusEnum.INVEST_NO_DISTRBUTE.getCode());
           int saveDisResult = distributionMapper.insert(distribution);
           if(saveDisResult <=0){
               log.error("产生默认分销记录失败");
               throw new RuntimeException();
           }

           invest.setRewardBalance(reward.getRegisterSelf());
           BalanceStatement statement = new BalanceStatement();
           statement.setAmount(reward.getRegisterSelf());
           statement.setUserId(user.getId());
           statement.setWay(BalanceStatementTypeEnum.REWARD.getCode());
           statement.setType(DistributionTypeEnum.LEVEL_ONE.getCode());
           statement.setCreatedTime(new Date());
           statement.setDistributionId(distribution.getId());
           int saveStatementResult = balanceStatementMapper.insert(statement);
           if(saveStatementResult <=0){
               log.error("不分销，保存余额变动失败");
               throw new RuntimeException();
           }
       }


        return user;
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public Boolean updatePassword(String newPassword,User user) {
        user.setPassword(newPassword);
        int result = userMapper.updateByPrimaryKey(user);
        if(result == 1)return true;
        return false;
    }

    @Override
    public TokenBase saveToken(User user,String userAgent, String ipAddress) {
        TokenBase tokenBase = new TokenBase(user.getId(),generateTokenData(), new Date(),ipAddress,userAgent);
        tokenBaseMapper.deleteByPrimaryKey(user.getId());
        tokenBaseMapper.insert(tokenBase);
        return tokenBase;
    }

    @Override
    public Boolean logout(String token) {
        int result = tokenBaseMapper.deleteByTokenValue(token);
        if(result > 0){
            return true;
        }
        return false;
    }


    @Override
    public ResponseVo updateHeadPic(MultipartFile uploadFile,Integer userId){
        String fileType = "";
        String fileName = uploadFile.getOriginalFilename();
        if(null != uploadFile && uploadFile.getSize() > 51200){
            return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_SIZE_LIMIT,null);
        }
        if(!RegexUtil.isPic(fileName)){
            return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
        }
        fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String newName = DateUtil.getTimeStamp()+"."+fileType;
        String newDir = Constants.HEAD_CUT_PATH+userId+"/";
        String newPath = newDir+newName;
        ErrorCodeEnum saveCode = fileUtil.saveFile(uploadFile,newDir,newName);
        if(!saveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(saveCode,null);

        User user = userMapper.get(userId);
        user.setHimageUrl(newPath);
        int userResult = userMapper.updateByPrimaryKey(user);
        if(userResult <=0)return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        String url = uploadProperties.getUrl(newPath);
        return new ResponseVo(ErrorCodeEnum.SUCCESS,new UploadHeadImageInfoVo(url));
    }

    private String generateTokenData() {
        byte[] newToken = new byte[Constants.DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }


}
