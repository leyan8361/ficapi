//package com.fic.service.controller.api;
//
//import com.fic.service.Enum.ErrorCodeEnum;
//import com.fic.service.Vo.ResponseVo;
//import com.fic.service.entity.User;
//import com.fic.service.mapper.UserMapper;
//import com.fic.service.service.SmsService;
//import com.fic.service.utils.RegexUtil;
//import io.swagger.annotations.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// *   @Author Xie
// *   @Date 2018/11/28
// *   @Discription:
//**/
//@RestController
//@RequestMapping("/api/v1")
//@Api("Api-短信")
//public class ApiSmsController {
//
//    private final Logger log = LoggerFactory.getLogger(ApiSmsController.class);
//
//    @Autowired
//    SmsService smsService;
//    @Autowired
//    UserMapper userMapper;
//
//    @GetMapping("/sendSms")
//    @ApiOperation("Api-发送")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "string", name = "telephone", value = "手机号", required = true)
//    })
//    @ApiResponses({
//            @ApiResponse(code = 1001, message = "USER_NOT_EXIST"),
//            @ApiResponse(code = 1012, message = "INVALID TELEPHONE"),
//            @ApiResponse(code = 1017, message = "REST ERROR"),
//            @ApiResponse(code = 1016, message = "SMS_SEND_COUNT_LIMIT"),
//            @ApiResponse(code = 500, message = "System ERROR"),
//            @ApiResponse(code = 200, message = "SUCCESS")
//    })
//    public ResponseEntity sendSms(@RequestParam String telephone) {
//        log.debug(" do sendSms action !!");
//        boolean isPhone = RegexUtil.isPhone(telephone);
//        if(!isPhone)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.INVALID_TELEPHONE,null));
//        User user = userMapper.findByUsername(telephone);
//        if(null == user)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null));
//        ErrorCodeEnum result = smsService.send(telephone);
//        return ResponseEntity.ok(new ResponseVo(result,null));
//    }
//
//    @GetMapping("/checkCode")
//    @ApiOperation("Api-测试验证码")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "string", name = "telephone", value = "手机号", required = true),
//            @ApiImplicitParam(dataType = "string", name = "code", value = "手机号", required = true)
//    })
//    public ResponseEntity checkCode(@RequestParam String telephone,@RequestParam String code) {
//        log.debug(" do checkCode action !!");
//        ErrorCodeEnum errorCodeEnum = smsService.check(telephone,code);
//        return ResponseEntity.ok(new ResponseVo(errorCodeEnum,null));
//    }
//
//}
