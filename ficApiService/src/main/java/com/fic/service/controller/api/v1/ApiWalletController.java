package com.fic.service.controller.api.v1;


import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.*;
import com.fic.service.service.TransactionRecordService;
import com.fic.service.service.WalletService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/wallet")
@Api(description = "Api-钱包相关")
public class ApiWalletController {

    private final Logger log = LoggerFactory.getLogger(ApiWalletController.class);

    @Autowired
    WalletService walletService;
    @Autowired
    TransactionRecordService transactionRecordService;

    @GetMapping("/getBalance")
    @ApiOperation("Api-查询钱包余额")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = BigInteger.class)
    })
    public ResponseEntity getBalance(@RequestParam int userId) {
        log.debug(" do getTokenBalance action !!");
        ResponseVo result = walletService.queryBalanceByUserId(userId);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }

    @PostMapping("/doTransactionOutApply")
    @ApiOperation("Api-申请转出")
    @ApiResponses({
            @ApiResponse(code = 6005, message = "PAYEE_NOT_EXIST"),
            @ApiResponse(code = 6006, message = "TRAN_CAN_NOT_TO_SELF"),
            @ApiResponse(code = 2001, message = "INVEST_BALANCE_NOT_ENOUGH"),
            @ApiResponse(code = 200, message = "SUCCESS",response = BigInteger.class)
    })
    public ResponseEntity doTransactionOutApply(@RequestBody DoTranTokenVo transactionVo) {
        log.debug(" doTransactionOutApply action !!");
        ResponseVo result = transactionRecordService.doTransactionApply(transactionVo);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getTransactionRecord")
    @ApiOperation("Api-获取转账记录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "int", name = "pageNum", value = "页码", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = AppTransactionRecordVo.class)
    })
    public ResponseEntity getTransactionRecord(@RequestParam Integer userId,@RequestParam int pageNum) {
        log.debug(" doTransactionOutApply action !!");
        ResponseVo result = transactionRecordService.getTransactionRecord(userId,pageNum);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addWalletAddress")
    @ApiOperation("Api-添加钱包地址")
    @ApiResponses({
            @ApiResponse(code = 6007, message = "ADDRESS_EXIST"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity addWalletAddress(@RequestBody WalletAddAddressVo walletAddAddressVo) {
        log.debug(" addWalletAddress action !!");
        ResponseVo result = transactionRecordService.addAddress(walletAddAddressVo);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/getAllAddress")
    @ApiOperation("Api-获取所有地址")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = AddressVo.class)
    })
    public ResponseEntity getAllAddress(@RequestParam Integer userId) {
        log.debug(" getAllAddress action !!");
        ResponseVo result = transactionRecordService.getAllAddress(userId);
        return ResponseEntity.ok(result);
    }

}
