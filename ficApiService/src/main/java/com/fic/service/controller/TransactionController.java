package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.TransactionStatusEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.TransactionRecord;
import com.fic.service.entity.Wallet;
import com.fic.service.mapper.TransactionRecordMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.TransactionRecordService;
import com.fic.service.service.WalletService;
import io.swagger.annotations.*;
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
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@RestController
@RequestMapping("/backend/transaction")
@Api(description = "转出转出管理")
public class TransactionController {

    private final Logger log = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    WalletService walletService;
    @Autowired
    TransactionRecordMapper transactionRecordMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    TransactionRecordService transactionRecordService;

    @GetMapping("/getAllTransaction")
    @ApiOperation("Api-查询所有转账记录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "type", value = "(0,查看全部，1查看转入,2查看转出)", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity getAllTransaction(@RequestParam int type) {
        log.debug(" getAllTransaction Action !!!");
        List<TransactionRecord> resultList = transactionRecordMapper.findAllByType(type);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
    }

    @GetMapping("/approveTransaction")
    @ApiOperation("Api-审核转账申请")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "转账ID", required = true),
            @ApiImplicitParam(dataType = "boolean", name = "approve", value = "(true,通过)(false,不通过)",required = true),
            @ApiImplicitParam(dataType = "string", name = "remark", value = "备注"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity approveTransaction(@RequestParam int id,@RequestParam boolean approve,@RequestParam String remark) {
        log.debug(" approveTransaction Action !!!");
        TransactionRecord result = transactionRecordMapper.selectByPrimaryKey(id);
        if(null == result){
            log.error(" transaction not found id:{}",id);
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.TRANSACTION_NOT_FOUND,null));
        }
        ResponseVo response = null;
        if(approve){
            response = transactionRecordService.approve(id,remark);
        }else{
            response = transactionRecordService.reject(id,remark);
        }
        return ResponseEntity.ok(response);
    }
}
