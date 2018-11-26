package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.LoginUserInfoVo;
import com.fic.service.Vo.RegisterUserInfoVo;
import com.fic.service.Vo.ResetPasswordInfo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.controller.HomeController;
import com.fic.service.entity.User;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.AccountService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Xie
 * @Date 2018/11/23
 * @Discription:
 **/
@RestController
@RequestMapping("/api/v1")
@Api("Api-账户相关，登录、登出、注册、获取账户信息")
public class ApiAccountController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserMapper userMapper;
    @Autowired
    AccountService accountService;

    @GetMapping("/login")
    @ApiOperation("Api-登录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(dataType = "String", name = "password", value = "密码md5结果", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1001, message = "User Not Exist"),
            @ApiResponse(code = 1001, message = "Password UnMatch"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity login(HttpServletRequest request, HttpServletResponse response, @RequestParam String username, @RequestParam String password) {
        log.debug(" Api Login Action !!!");
        if(StringUtils.isEmpty(username))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null));
        User user = userMapper.findByUsername(username);
        if(null == user)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null));
        if(!password.equals(user.getPassword()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PASSWORD_NOT_MATCH,null));
        LoginUserInfoVo result = accountService.login(request,user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    @ApiOperation("Api-注册")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1007,message = "USER EXIST"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity register(HttpServletRequest request, HttpServletResponse response, @RequestBody RegisterUserInfoVo userInfoVo) {
        log.debug(" Api register Action !!!");
        if(StringUtils.isEmpty(userInfoVo.getUsername()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null));
        User checkUser = userMapper.findByUsername(userInfoVo.getUsername());
        if(null != checkUser)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USERNAME_EXIST,null));
        User user = accountService.register(userInfoVo);
        if(null == user)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
        LoginUserInfoVo result = accountService.login(request,user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/updatePassword")
    @ApiOperation("Api-修改密码,将刷新Token")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1001, message = "User Not Exist"),
            @ApiResponse(code = 1008, message = "OLD PASSWORD NOT MATCH"),
            @ApiResponse(code = 1009,message = "NEW PASSWORD NOT MATCH WITH RE"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity updatePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody ResetPasswordInfo userInfoVo){
        log.debug(" Api Update Password Action !!!");
        if(StringUtils.isEmpty(userInfoVo.getUsername()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null));
        User checkUser = userMapper.findByUsername(userInfoVo.getUsername());
        if(null == checkUser)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null));
        if(!checkUser.getPassword().equals(userInfoVo.getOldPassword()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.OLD_PASSWORD_NOT_MATCH,null));
        if(!userInfoVo.getNewPassword().equals(userInfoVo.getRePassword()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.RE_PASSWORD_NOT_MATCH,null));
        boolean result = accountService.updatePassword(userInfoVo.getNewPassword(),checkUser);
        LoginUserInfoVo refreshUser = null;
        if(result){
            refreshUser = accountService.login(request,checkUser);
            return ResponseEntity.ok(refreshUser);
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
    }

    @GetMapping("/resetPassword")
    @ApiOperation("Api-重置密码,将刷新Token")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(dataType = "String", name = "newpassword", value = "新密码MD5结果", required = true),
            @ApiImplicitParam(dataType = "String", name = "validateCode", value = "短信验证码", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1001, message = "User Not Exist"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity resetPassword(HttpServletRequest request,@RequestParam String username,@RequestParam String newpassword,@RequestParam String validateCode){
        log.debug(" Api Reset Password Action !!!");
        if(StringUtils.isEmpty(username))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null));
        User checkUser = userMapper.findByUsername(username);
        if(null != checkUser)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null));
        //TODO Check validate
        boolean result = accountService.updatePassword(newpassword,checkUser);
        LoginUserInfoVo refreshUser = null;
        if(result){
            refreshUser = accountService.login(request,checkUser);
            return ResponseEntity.ok(refreshUser);
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
    }

    @GetMapping("/logout")
    @ApiOperation("Api-登出,失效Token")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 500, message = "System ERROR")
    })
    public ResponseEntity logout(HttpServletRequest request){
        log.debug(" Api logout Action !!!");
        String token = request.getHeader(Constants.TOKEN_KEY);
        if(StringUtils.isEmpty(token)){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.TOKEN_MISSED_HEADER,null));
        }
        boolean result = accountService.logout(token);
        if(result)return ResponseEntity.ok().build();
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
    }




}