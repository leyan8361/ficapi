package com.fic.service.service.impl;

import com.fic.service.Enum.*;
import com.fic.service.Vo.*;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.controller.HomeController;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.AccountService;
import com.fic.service.service.RewardService;
import com.fic.service.service.WalletService;
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
    @Autowired
    WalletService walletService;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    UserAuthMapper userAuthMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public LoginUserInfoVo login(HttpServletRequest request,User user) {
        LoginUserInfoVo loginUserInfoVo = new LoginUserInfoVo();
        loginUserInfoVo.setUserId(user.getId());
        loginUserInfoVo.setHimageUrl(uploadProperties.getUrl(user.getHimageUrl()));
        loginUserInfoVo.setMyInviteCode(user.getUserInviteCode());
        loginUserInfoVo.setUsername(user.getUserName());
        loginUserInfoVo.setNickName(user.getNickName());
        loginUserInfoVo.setEmail(user.getEmail());
        UserAuth userAuth = userAuthMapper.findByUserId(user.getId());
        if(null == userAuth){
            loginUserInfoVo.setAuthStatus(0);
        }else{
            if(userAuth.getStatus() == 0){
                loginUserInfoVo.setAuthStatus(1);
            }
            if(userAuth.getStatus() == 1){
                loginUserInfoVo.setAuthStatus(2);
            }
            if(userAuth.getStatus() == 2){
                loginUserInfoVo.setAuthStatus(3);
            }
        }
        if(StringUtils.isEmpty(user.getPayPassword())){
            loginUserInfoVo.setSetPayPassword(false);
        }else{
            loginUserInfoVo.setSetPayPassword(true);
        }
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

        Integer lastInsertId = userMapper.getLastInsertID() + 1;
        User user = new User();
        user.setUserName(userInfoVo.getUsername());
        user.setPassword(userInfoVo.getPassword());
        user.setTuserInviteCode(userInfoVo.getInviteCode());
        user.setUserInviteCode(InviteCodeUtil.toSerialCode(lastInsertId));
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        user.setNickName(Constants.NICK_NAME);
        user.setHimageUrl(Constants.DEFAULT_HEAD_PATH);
//        String walletAddress = walletService.generateWalletAddress(lastInsertId,userInfoVo.getPassword());
//        user.setWalletAddress(walletAddress);
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


        /**
         * 设备
         */
        Device device = new Device();
        device.setDeviceCode(userInfoVo.getDeviceCode());
        device.setCreatedTime(new Date());
        device.setLastedLoginTime(new Date());
        device.setLoginCount(0);
        int deviceSaveResult = deviceMapper.insertSelective(device);
        if(deviceSaveResult <=0){
            log.error("保存设备编号失败 : {} ",device.getDeviceCode());
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

           boolean disResult = rewardService.distributionRewardByAction(user,inviteByWho,invest,true,null);
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
           statement.setWay(FinanceWayEnum.IN.getCode());
           statement.setType(FinanceTypeEnum.REWARD.getCode());
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

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo updateUserName(int userId, String telephone) {
        User user = userMapper.get(userId);
        if(null == user){
            log.error("更新手机号失败，用户不存在");
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        String existTel = user.getUserName();
        if(telephone.equals(existTel)){
            return new ResponseVo(ErrorCodeEnum.TELEPHONE_IS_BEING_USED,null);
        }
        int checkIfExist = userMapper.checkIfExistByTelephone(telephone);
        if(checkIfExist >0){
            log.error("用户手机号重复,telephone :{}",telephone);
            return new ResponseVo(ErrorCodeEnum.TELEPHONE_EXIST,null);
        }

        int updateResult = userMapper.updateUserName(userId,telephone);
        if(updateResult <=0){
            log.error("更新手机号失败，userId:{}",userId);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo updateEmail(int userId, String email,String password) {
        User user = userMapper.get(userId);
        if(null == user){
            log.error("更新邮箱失败，用户不存在");
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        String existEmail = user.getEmail();
        if(email.equals(existEmail)){
            return new ResponseVo(ErrorCodeEnum.EMAIL_IS_BEING_USED,null);
        }
        int checkIfExist = userMapper.checkIfExistByEmail(email);
        if(checkIfExist >0){
            log.error("用户邮箱重复,telephone :{}",email);
            return new ResponseVo(ErrorCodeEnum.EMAIL_EXIST,null);
        }

        if(!user.getPassword().equals(password)){
            log.debug("绑定邮箱，登录密码错误");
            return new ResponseVo(ErrorCodeEnum.PASSWORD_NOT_MATCH,null);
        }

        int updateResult = userMapper.updateEmail(userId,email);
        if(updateResult <=0){
            log.error("更新邮箱失败，userId:{}",userId);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo getUserInfo(int userId) {
        LoginUserInfoVo result = new LoginUserInfoVo();
        User user = userMapper.get(userId);
        if(null == user){
            log.error("查找用户信息失败 user id :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        UserAuth userAuth = userAuthMapper.findByUserId(userId);
        result.setUserId(userId);
        if(null == userAuth){
            result.setAuthStatus(0);
        }else{
            if(userAuth.getStatus() == 0){
                result.setAuthStatus(1);
            }
            if(userAuth.getStatus() == 1){
                result.setAuthStatus(2);
            }
            if(userAuth.getStatus() == 2){
                result.setAuthStatus(3);
            }
        }
        if(StringUtils.isEmpty(user.getPayPassword())){
            result.setSetPayPassword(false);
        }else{
            result.setSetPayPassword(true);
        }
        result.setEmail(user.getEmail());
        result.setNickName(user.getNickName());
        result.setHimageUrl(uploadProperties.getUrl(user.getHimageUrl()));
        result.setMyInviteCode(user.getUserInviteCode());
        result.setUsername(user.getUserName());
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo updatePayPassword(int userId, String oldPayPassword, String newPayPassword) {
        User user = userMapper.get(userId);
        if(null == user){
            log.error("修改密码失败, user id :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        if(StringUtils.isEmpty(oldPayPassword) || !oldPayPassword.equals(user.getPayPassword())){
            return new ResponseVo(ErrorCodeEnum.USER_PAY_PASSWORD_NOT_MATCH,null);
        }
        user.setPayPassword(newPayPassword);
        int updateResult = userMapper.updateByPrimaryKey(user);
        if(updateResult <=0){
            log.error("更新支付密码失败，user id :{}",user.getId());
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
