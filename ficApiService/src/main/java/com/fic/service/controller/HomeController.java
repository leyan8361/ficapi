package com.fic.service.controller;

import com.fic.service.Enum.CoinUSDEnum;
import com.fic.service.Vo.QueryTransactionResultVo;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.BalanceStatement;
import com.fic.service.entity.TickerRecord;
import com.fic.service.mapper.BalanceStatementMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.scheduled.BetScheduledService;
import com.fic.service.scheduled.TransactionScheduledService;
import com.fic.service.service.MaoYanService;
import com.fic.service.service.SmsService;
import com.fic.service.service.TransactionRecordService;
import com.fic.service.service.WalletService;
import com.fic.service.utils.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    TransactionScheduledService transactionScheduledService;
    @Autowired
    OkCoinUtil okCoinUtil;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    BalanceStatementMapper balanceStatementMapper;

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

    @GetMapping("/testTranIfIn")
    @ApiOperation("查询转出是否成功")
    public ResponseEntity testTranIfIn(@RequestParam int tranOutId) {
        log.debug(" testTranIfIn !!!");
        transactionScheduledService.doQueryTransactionStatus();
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/transaction")
    @ApiOperation("测试转账")
    public ResponseEntity transaction(@RequestParam(required = false)String txHash,@RequestParam(required = false) int userId,@RequestParam(required = false) BigDecimal amount,@RequestParam(required = false) String toAddress) {
        log.debug(" transaction !!!");
//         List<String> result = web3jUtil.getAccountlist();
//        web3jUtil.unLock("0x937b3080025cdae1a7e9f564405ecc29beeaa181","f379eaf3c831b04de153469d1bec345e");
//        web3jUtil.getEthBalance(toAddress);
//        QueryTransactionResultVo result = web3jUtil.queryTransactionStatus(txHash,new BigDecimal(0.000000004));
//        log.debug(" hash : {} ，状态 : {}, gasFee :{}",txHash,result.getStatus(),result.getGasUsed());
//        transactionRecordService.doTransactionOut(userId,amount,toAddress);
        return ResponseEntity.ok().body("success");
    }
    @GetMapping("/testOkCoinAPi")
    @ApiOperation("测试okCoinApi")
    public ResponseEntity testOkCoinAPi() {
        log.debug(" testOkCoinAPi !!!");
        TickerRecord result = okCoinUtil.getTicker(CoinUSDEnum.USDT);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/admin")
    @ApiOperation("测试管理员用户权限")
    public ResponseEntity admin() {
        log.debug(" Admin Page !!!");
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/testDistributionBalance")
    public ResponseEntity testDistributionBalance() {

        List<BalanceStatement> balanceStatements = balanceStatementMapper.findAll();

        Map<String,List<BalanceStatement>> needSortMap = new HashMap<>();

        for(BalanceStatement ba :  balanceStatements){
            if(null == ba.getAmount() && null!=ba.getInvestDetailId()){
                continue;
            }
            if(needSortMap.containsKey(ba.getUserId()+"")){
                needSortMap.get(ba.getUserId()+"").add(ba);
            }else{
                List<BalanceStatement> newBalan = new ArrayList<>();
                newBalan.add(ba);
                needSortMap.put(ba.getUserId()+"",newBalan);
            }
        }

        int count = 0;
        for(Map.Entry<String,List<BalanceStatement>> map : needSortMap.entrySet()){
            List<BalanceStatement> sortList = map.getValue();
            if(sortList.size() > 1){
                for(int i = 0 ; i < sortList.size(); i++){
                    for(int j = 0; j < sortList.size()-1-i; j++){
                        if(sortList.get(j).getCreatedTime().compareTo(sortList.get(j+1).getCreatedTime()) >0){
                            BalanceStatement temp = sortList.get(j);
                            sortList.set(j,sortList.get(j+1));
                            sortList.set(j+1,temp);
                        }
                    }
                }
            }
            count = count+1;
//            if(map.getKey().equals(114+"")){
//                for(BalanceStatement result: sortList){
                    System.out.println("用户ID : "+map.getKey() + " | " + " 余额 第一条 时间 : " + DateUtil.dateToStrMatSec(sortList.get(0).getCreatedTime()) +" 金额  : " + sortList.get(0).getAmount());
//                }
//            }
         }


        System.out.println("总数 : " + count);
        return ResponseEntity.ok().body("success");
    }

}
