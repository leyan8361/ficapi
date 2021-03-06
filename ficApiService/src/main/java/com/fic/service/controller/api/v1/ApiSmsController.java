package com.fic.service.controller.api.v1;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.SmsService;
import com.fic.service.utils.EmailUtil;
import com.fic.service.utils.RegexUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *   @Author Xie
 *   @Date 2018/11/28
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1")
@Api(description = "Api-短信")
public class ApiSmsController {

    private final Logger log = LoggerFactory.getLogger(ApiSmsController.class);

    @Autowired
    SmsService smsService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailUtil emailUtil;

    @GetMapping("/sendSms")
    @ApiOperation("Api-发送")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "telephone", value = "手机号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1012, message = "INVALID TELEPHONE"),
            @ApiResponse(code = 1017, message = "REST ERROR"),
            @ApiResponse(code = 1016, message = "SMS_SEND_COUNT_LIMIT"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity sendSms(@RequestParam String telephone) {
        log.debug(" do sendSms action !!");
        boolean isPhone = RegexUtil.isPhone(telephone);
        if(!isPhone)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.INVALID_TELEPHONE,null));
        ErrorCodeEnum result = smsService.send(telephone);
        return ResponseEntity.ok(new ResponseVo(result,null));
    }


    @GetMapping("/sendEmail")
    @ApiOperation("Api-发送邮件")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "email", value = "邮箱地址", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1034, message = "NOT_A_EMAIL"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity sendEmail(@RequestParam String email) {
        log.debug(" do sendEmail action !!");
        ErrorCodeEnum result = smsService.sendMail(email);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }

    @GetMapping("/checkCode")
    @ApiOperation("Api-Check验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "telephone", value = "手机号 or Email", required = true),
            @ApiImplicitParam(dataType = "string", name = "code", value = "验证码", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1013, message = "VALIDATE_CODE_INVALID"),
            @ApiResponse(code = 1014, message = "VALIDATE_CODE_EXPIRED"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity checkCode(@RequestParam String telephone,@RequestParam String code) {
        log.debug(" do checkCode action !!");
        ErrorCodeEnum errorCodeEnum = smsService.check(telephone,code);
        return ResponseEntity.ok(new ResponseVo(errorCodeEnum,null));
    }

}
