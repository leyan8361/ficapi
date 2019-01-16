package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.TransactionStatusEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
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

import java.math.BigDecimal;
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
    @ApiOperation("Api-审核转出申请")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "转账ID", required = true),
            @ApiImplicitParam(dataType = "boolean", name = "approve", value = "(true,通过)(false,不通过)",required = true),
            @ApiImplicitParam(dataType = "string", name = "remark", value = "备注"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity approveTransaction(@RequestParam int id,@RequestParam boolean approve,@RequestParam(required = false) String remark) {
        log.debug(" approveTransaction Action !!!");
        TransactionRecord result = transactionRecordMapper.selectByPrimaryKey(id);
        if(null == result){
            log.error(" transaction not found id:{}",id);
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.TRANSACTION_NOT_FOUND,null));
        }
        ResponseVo response = null;
        if(approve){
            response = transactionRecordService.approveForTFC(id,remark);
        }else{
            response = transactionRecordService.rejectForTFC(id,remark);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirmTranIn")
    @ApiOperation("Api-确认转入")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "fromAddress", value = "转入来源地址"),
            @ApiImplicitParam(dataType = "string", name = "txHash", value = "交易合约Hash"),
//            @ApiImplicitParam(dataType = "string", name = "coinType", value = "币种",required = true),
            @ApiImplicitParam(dataType = "int", name = "amount", value = "到账数量"),
            @ApiImplicitParam(dataType = "string", name = "inComeTime", value = "到账时间,格式(yyyy-MM-dd HH:mm:ss)",required = true,example = "2019-01-16 12:00:00"),
            @ApiImplicitParam(dataType = "string", name = "remark", value = "备注",required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity confirmTranIn(@RequestParam int userId, @RequestParam(required = false) String fromAddress, @RequestParam(required = false) String txHash, @RequestParam int amount,@RequestParam(required = false) String remark,@RequestParam String inComeTime) {
        log.debug(" confirmTranIn Action !!!");
        BigDecimal amountDecimal = new BigDecimal(amount);
        ResponseVo result = transactionRecordService.confirmTranIn(userId,fromAddress,txHash, Constants.TFC,amountDecimal,remark,inComeTime);
        return ResponseEntity.ok(result);
    }
}
