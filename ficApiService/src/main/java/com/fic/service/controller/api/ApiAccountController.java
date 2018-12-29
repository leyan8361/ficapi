package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.*;
import com.fic.service.constants.Constants;
import com.fic.service.controller.HomeController;
import com.fic.service.entity.User;
import com.fic.service.mapper.DeviceMapper;
import com.fic.service.mapper.TokenBaseMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.AccountService;
import com.fic.service.service.DistributionService;
import com.fic.service.utils.RegexUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author Xie
 * @Date 2018/11/23
 * @Discription:
 **/
@RestController
@RequestMapping("/api/v1")
@Api(description = "Api-账户相关，登录、登出、注册、获取账户信息")
public class ApiAccountController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserMapper userMapper;
    @Autowired
    AccountService accountService;
    @Autowired
    DistributionService distributionService;
    @Autowired
    DeviceMapper deviceMapper;

    @GetMapping("/login")
    @ApiOperation("Api-登录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(dataType = "String", name = "password", value = "密码md5结果", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1001, message = "User Not Exist"),
            @ApiResponse(code = 1000, message = "Password UnMatch"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity login(HttpServletRequest request, HttpServletResponse response, @RequestParam String username, @RequestParam String password) {
        log.debug(" Api Login Action !!!");
        if(StringUtils.isEmpty(username))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null));
        User user = userMapper.findByUsername(username);
        if(null == user)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null));
        if(!password.equals(user.getPassword()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PASSWORD_NOT_MATCH,null));
        LoginUserInfoVo result = accountService.login(request,user);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }

    @PostMapping("/register")
    @ApiOperation("Api-注册")
    @ApiResponses({
            @ApiResponse(code = 1023,message = "DEVICE_EXCEPTION"),
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1007,message = "USER EXIST"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity register(HttpServletRequest request, HttpServletResponse response, @RequestBody RegisterUserInfoVo userInfoVo) {
        log.debug(" Api register Action !!!");
        if(StringUtils.isEmpty(userInfoVo.getUsername()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null));
        int existRegisterDevice = deviceMapper.checkSameDevice(userInfoVo.getDeviceCode());
        if(existRegisterDevice > 1)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.DEVICE_EXCEPTION,null));
        User checkUser = userMapper.findByUsername(userInfoVo.getUsername());
        if(null != checkUser)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USERNAME_EXIST,null));
        if(StringUtils.isNotEmpty(userInfoVo.getInviteCode())){
            User checkInviteUserExist = userMapper.findByInviteCode(userInfoVo.getInviteCode());
            if(null == checkInviteUserExist)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.INVITE_CODE_NOT_EXIST,null));
        }
        User user = accountService.register(userInfoVo);
        if(null == user)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
        LoginUserInfoVo result = accountService.login(request,user);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }

    @PostMapping("/updatePassword")
    @ApiOperation("Api-修改密码")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1001, message = "User Not Exist"),
            @ApiResponse(code = 1008, message = "OLD PASSWORD NOT MATCH"),
            @ApiResponse(code = 1009,message = "NEW PASSWORD NOT MATCH WITH RE"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity updatePassword(@RequestBody ResetPasswordInfo userInfoVo){
        log.debug(" Api Update Password Action !!!");
        if(StringUtils.isEmpty(userInfoVo.getUsername()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null));
        User checkUser = userMapper.findByUsername(userInfoVo.getUsername());
        if(null == checkUser)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null));
        if(!checkUser.getPassword().equals(userInfoVo.getOldPassword()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.OLD_PASSWORD_NOT_MATCH,null));
        if(!userInfoVo.getNewPassword().equals(userInfoVo.getRePassword()))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.RE_PASSWORD_NOT_MATCH,null));
        boolean result = accountService.updatePassword(userInfoVo.getNewPassword(),checkUser);
        LoginUserInfoVo refreshUser = null;
        if(result){
//            refreshUser = accountService.login(request,checkUser);
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,null));
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
    }

    @GetMapping("/resetPassword")
    @ApiOperation("Api-重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(dataType = "String", name = "newpassword", value = "新密码MD5结果", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1001, message = "User Not Exist"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity resetPassword(HttpServletRequest request,@RequestParam String username,@RequestParam String newpassword){
        log.debug(" Api Reset Password Action !!!");
        if(StringUtils.isEmpty(username))return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null));
        User checkUser = userMapper.findByUsername(username);
        if(null == checkUser)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null));
        boolean result = accountService.updatePassword(newpassword,checkUser);
        LoginUserInfoVo refreshUser = null;
        if(result){
//            refreshUser = accountService.login(request,checkUser);
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,null));
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
        if(result)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,null));
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
    }

    @PostMapping(value = "/updateHeadPic",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiOperation("Api-更新头像")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 1019, message = "UPLOAD FAILED,SYSTEM_ERROR"),
            @ApiResponse(code = 1024, message = "5120KB LIMIT"),
            @ApiResponse(code = 200, message = "SUCCESS",response = UploadHeadImageInfoVo.class)
    })
    public ResponseEntity updateHeadPic(@RequestParam Integer userId,@ApiParam(value = "头像",required = true) MultipartFile file){
        log.debug(" update Head Pic !!!");
        ResponseVo response = accountService.updateHeadPic(file,userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/setPayPassword")
    @ApiOperation("Api-设置或更新支付密码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "payPassword", value = "支付密码MD5结果", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity resetPayPassword(@RequestParam Integer userId,@RequestParam String payPassword){
        log.debug(" do setPayPassword Action !!!");
        User user = userMapper.get(userId);
        user.setPayPassword(payPassword);
        int result = userMapper.updateByPrimaryKey(user);
        if(result <=0){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,null));
    }


    @GetMapping(value = "/checkPayPassword")
    @ApiOperation("Api-校对支付密码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "payPassword", value = "支付密码MD5结果", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1020, message = "USER_PAY_PASSWORD_NOT_SET"),
            @ApiResponse(code = 1020, message = "USER_PAY_PASSWORD_NOT_MATCH"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity checkPayPassword(@RequestParam Integer userId,@RequestParam String payPassword){
        log.debug(" do checkPayPassword Action !!!");
        User user = userMapper.get(userId);

        if(StringUtils.isEmpty(user.getPayPassword())){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_PAY_PASSWORD_NOT_SET,null));
        }
        if(!payPassword.equals(user.getPayPassword())){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_PAY_PASSWORD_NOT_MATCH,null));
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,null));
    }


    @PostMapping(value = "/updateUserInfo")
    @ApiOperation("Api-修改用户信息")
    @ApiResponses({
            @ApiResponse(code = 1020, message = "USER_PAY_PASSWORD_NOT_SET"),
            @ApiResponse(code = 1020, message = "USER_PAY_PASSWORD_NOT_MATCH"),
            @ApiResponse(code = 200, message = "SUCCESS",response = UpdateUserInfoVo.class)
    })
    public ResponseEntity updateUserInfo(@RequestBody UpdateUserInfoVo userInfoVo){
        log.debug(" do updateUserInfo Action !!!");
        User user = userMapper.get(userInfoVo.getUserId());
        if(null == user){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null));
        }
        if(StringUtils.isNotEmpty(userInfoVo.getNickName())){
            user.setNickName(userInfoVo.getNickName());
        }
        if(StringUtils.isNotEmpty(userInfoVo.getEmail())){
            user.setEmail(userInfoVo.getEmail());
        }
        if(null != userInfoVo.getZip()){
            user.setZip(userInfoVo.getZip());
        }
        if(StringUtils.isNotEmpty(userInfoVo.getTelephone())){
            if(!RegexUtil.isPhone(userInfoVo.getTelephone())){
                return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.NOT_A_TELEPHONE,null));
            }
            user.setUserName(userInfoVo.getTelephone());
        }
        int saveResult = userMapper.updateByPrimaryKey(user);
        if(saveResult <=0)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));

        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,userInfoVo));
    }


    @GetMapping(value = "/getInviteGroup")
    @ApiOperation("Api-邀请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity getInviteGroup(@RequestParam Integer userId){
        log.debug(" do getInviteGroup Action !!!");
        List<DistributionVo> resultList = distributionService.getMyDistributionRecord(userId);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
    }


}
