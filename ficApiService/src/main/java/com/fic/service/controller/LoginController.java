package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.mapper.AdminUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Xie
 * @Date 2018/11/22
 * @Discription:
 **/
@RestController
@RequestMapping("/backend")
@Api(description = "登录登出")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    AdminUserMapper adminUserMapper;

    @PostMapping("/login")
    @ApiOperation("登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(dataType = "String", name = "password", value = "密码md5结果", required = true)
    })
    public ResponseEntity login(@RequestParam String username,@RequestParam String password) {
        log.debug("login Action!!");
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            currentUser.login(token);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IncorrectCredentialsException e) {
            return ResponseEntity.status(ErrorCodeEnum.PASSWORD_NOT_MATCH.getCode()).body(ErrorCodeEnum.PASSWORD_NOT_MATCH.getMsg());
//        } catch (LockedAccountException e) {
//            //TODO 用户状态
        } catch (AuthenticationException e) {
            return ResponseEntity.status(ErrorCodeEnum.USER_NOT_EXIST.getCode()).body(ErrorCodeEnum.USER_NOT_EXIST.getMsg());
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCodeEnum.SYSTEM_EXCEPTION.getCode()).body(ErrorCodeEnum.SYSTEM_EXCEPTION.getMsg());
        }
    }


    @GetMapping("/loginSuccess")
    @ApiOperation("登录成功")
    public ResponseEntity loginSuccess() {
        log.debug("loginSuccess Action!!");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/logout")
    @ApiOperation("退出登录")
    public ResponseEntity logout() {
        log.debug("logout Action!!");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/unLogin")
    @ApiOperation("未登录")
    public ResponseEntity unLogin() {
        log.debug("unLogin Action!!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/unAuthorized")
    @ApiOperation("未授权")
    public ResponseEntity unauthorized() {
        log.debug("unAuthorized Action!!");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
