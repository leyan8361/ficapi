package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.DistributionVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.User;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.SmsService;
import com.fic.service.service.WalletService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription: Home Page
**/
@RestController
@RequestMapping("/backend")
@Api("首页")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    SmsService smsService;
    @Autowired
    WalletService walletService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ServerProperties serverProperties;
    @Autowired


    @GetMapping("/home")
    @ApiOperation("获取首页数据")
//    @RequiresAuthentication
    public ResponseEntity home() {
        System.out.println("index !!!!!");
        log.debug(" Home Page !!!");
//        if(1 ==1 ){
//            throw new RuntimeException();
//        }
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/user")
    @ApiOperation("测试普通用户权限")
    public ResponseEntity user() {
        log.debug(" User Page !!!");
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/admin")
    @ApiOperation("测试管理员用户权限")
    public ResponseEntity admin() {
        log.debug(" Admin Page !!!");
        return ResponseEntity.ok().body("success");
    }

//    @GetMapping(value = "/doMissingAddressMakeUp")
//    @ApiOperation("补充钱包地址，对象为注册无钱包地址的用户")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "SUCCESS")
//    })
//    public ResponseEntity doMissingAddressMakeUp(){
//        log.debug(" do doMissingAddressMakeUp Action !!!");
//        List<User> userList = userMapper.findMissingAddress();
//        String path = serverProperties.getStoreLocation();
//        if(userList.size() > 0){
//            for(User user : userList){
//                //TODO address make up
//                if(StringUtils.isEmpty(user.getWalletAddress())){
//                    String address = we
//                }
//            }
//        }
//
//        return ResponseEntity.ok().build();
//
//    }

}
