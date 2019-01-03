package com.fic.service.controller.api.v1;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.*;
import com.fic.service.constants.Constants;
import com.fic.service.entity.Invest;
import com.fic.service.mapper.InvestMapper;
import com.fic.service.service.DistributionService;
import com.fic.service.service.InvestService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1")
@Api(description = "Api-投资相关")
public class ApiInvestController {

    private final Logger log = LoggerFactory.getLogger(ApiInvestController.class);

    @Autowired
    InvestMapper investMapper;
    @Autowired
    InvestService investService;
    @Autowired
    DistributionService distributionService;

    @PostMapping("/invest")
    @ApiOperation("Api-投资")
    @ApiResponses({
            @ApiResponse(code = 2000, message = "INVEST NOT EXIST"),
            @ApiResponse(code = 2001, message = "INVEST_BALANCE_NOT_ENOUGH"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = InvestSuccessInfoVo.class)
    })
    public ResponseEntity invest(@RequestBody InvestInfoVo investInfoVo) {
        log.debug(" do invest action !!");
        Invest invest = investMapper.findByUserId(investInfoVo.getUserId());
        if(null == invest)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.INVEST_NOT_EXIST,null));

        BigDecimal investBalance = invest.getBalance().add(invest.getRewardBalance()).subtract(investInfoVo.getAmount()).setScale(Constants.KEEP_SCALE);
        if(BigDecimal.ZERO.compareTo(investBalance) >= 1)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.INVEST_BALANCE_NOT_ENOUGH,null));

        InvestSuccessInfoVo result = investService.invest(invest,investInfoVo);

        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }

    @GetMapping("/getInvestBalance")
    @ApiOperation("Api-获取投资余额")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2000, message = "INVEST NOT EXIST"),
            @ApiResponse(code = 200, message = "SUCCESS",response = InvestBalanceInfoVo.class)
    })
    public ResponseEntity getInvestBalance(@RequestParam Integer userId) {
        log.debug(" do get invest balance action !!");
        Invest invest = investMapper.findByUserId(userId);
        if(null == invest)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.INVEST_NOT_EXIST,null));
        InvestBalanceInfoVo balanceInfoVo = new InvestBalanceInfoVo();
        balanceInfoVo.setUserId(invest.getUserId());
        balanceInfoVo.setBalance(invest.getBalance().add(invest.getRewardBalance()).setScale(0,BigDecimal.ROUND_DOWN));
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,balanceInfoVo));
    }

    @GetMapping("/getInvestDetail")
    @ApiOperation("Api-获取投资记录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = InvestRecordInfoVo.class)
    })
    public ResponseEntity getInvestDetail(@RequestParam Integer userId) {
        log.debug(" do get invest detail action !!");
        InvestRecordInfoVo result = investService.getInvestDetail(userId);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }



}
