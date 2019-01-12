package com.fic.service.controller;

import com.fic.service.constants.ServerProperties;
import com.fic.service.mapper.UserMapper;
import com.fic.service.scheduled.BetScheduledService;
import com.fic.service.service.MaoYanService;
import com.fic.service.service.SmsService;
import com.fic.service.service.TransactionRecordService;
import com.fic.service.service.WalletService;
import com.fic.service.utils.Web3jUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription: Home Page
**/
@RestController
@RequestMapping("/backend")
@Api(description = "首页")
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
    MaoYanService maoYanService;
    @Autowired
    BetScheduledService betScheduledService;
    @Autowired
    Web3jUtil web3jUtil;
    @Autowired
    TransactionRecordService transactionRecordService;

    @GetMapping("/home")
    @ApiOperation("获取首页数据 , 拉票房，开奖")
    public ResponseEntity home() {
        System.out.println("index !!!!!");
        log.debug(" Home Page !!!");
        betScheduledService.doBoxPull();//拉数据
        betScheduledService.openPrice();
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/reward")
    @ApiOperation("分奖池")
//    @RequiresAuthentication
    public ResponseEntity pull() {
        System.out.println("reward !!!!!");
        betScheduledService.rewardPool();
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/returning")
    @ApiOperation("赔付")
    public ResponseEntity returning() {
        System.out.println("returning !!!!!");
        betScheduledService.makeUpReturning();
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/user")
    @ApiOperation("测试普通用户权限")
    public ResponseEntity user() {
        log.debug(" User Page !!!");
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/transaction")
    @ApiOperation("测试转账")
    public ResponseEntity transaction(@RequestParam(required = false)String txHash,@RequestParam(required = false) int userId,@RequestParam(required = false) BigDecimal amount,@RequestParam(required = false) String toAddress) {
        log.debug(" transaction !!!");
//         List<String> result = web3jUtil.getAccountlist();
//        web3jUtil.unLock("0x937b3080025cdae1a7e9f564405ecc29beeaa181","f379eaf3c831b04de153469d1bec345e");
        web3jUtil.getTokenBalance(toAddress);
//        int result = web3jUtil.queryTransactionStatus(txHash);
//        log.debug(" hash : {} ，状态 : {}",txHash,result);
//        transactionRecordService.doTransactionOut(userId,amount,toAddress);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/admin")
    @ApiOperation("测试管理员用户权限")
    public ResponseEntity admin() {
        log.debug(" Admin Page !!!");
        return ResponseEntity.ok().body("success");
    }


}
