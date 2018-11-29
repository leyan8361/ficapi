package com.fic.service.service;

import com.fic.service.Vo.LoginUserInfoVo;
import com.fic.service.Vo.RegisterUserInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.TokenBase;
import com.fic.service.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Xie
 * @Date 2018/11/26
 * @Discription:
 **/
public interface AccountService {

    /**
     * 登录
     * @param request
     * @param user
     * @return
     */
    LoginUserInfoVo login(HttpServletRequest request,User user);

    /**
     * 生成Token
     * @param user
     * @param userAgent
     * @param ipAddress
     * @return
     */
    TokenBase saveToken(User user,String userAgent, String ipAddress);

    /**
     * 注册并登录
     */
    User register(RegisterUserInfoVo userInfoVo);

    /**
     * 更改密码
     */
    Boolean updatePassword(String newPassword,User user);

    Boolean logout(String token);

    ResponseVo updateHeadPic(MultipartFile file,Integer userId);
}
