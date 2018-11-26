package com.fic.service.service.impl;

import com.fic.service.Vo.LoginUserInfoVo;
import com.fic.service.Vo.RegisterUserInfoVo;
import com.fic.service.Vo.ResetPasswordInfo;
import com.fic.service.constants.Constants;
import com.fic.service.controller.HomeController;
import com.fic.service.entity.TokenBase;
import com.fic.service.entity.User;
import com.fic.service.mapper.TokenBaseMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.AccountService;
import com.fic.service.utils.InviteCodeUtil;
import com.fic.service.utils.Sha1Util;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDate;
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

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public LoginUserInfoVo login(HttpServletRequest request,User user) {
        LoginUserInfoVo loginUserInfoVo = new LoginUserInfoVo();
        loginUserInfoVo.setUserId(user.getId());
        loginUserInfoVo.setHimageUrl(user.getHimageUrl());//TODO 加前缀
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

    private String generateTokenData() {
        byte[] newToken = new byte[Constants.DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }


}
