package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.InvestInfoVo;
import com.fic.service.Vo.LoginUserInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.entity.Invest;
import com.fic.service.entity.InvestDetail;
import com.fic.service.mapper.ExchangeRateMapper;
import com.fic.service.mapper.InvestDetailMapper;
import com.fic.service.mapper.InvestMapper;
import com.fic.service.mapper.MovieMapper;
import com.fic.service.service.InvestService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1")
@Api("Api-投资相关")
public class ApiInvestController {

    private final Logger log = LoggerFactory.getLogger(ApiInvestController.class);

    @Autowired
    ExchangeRateMapper exchangeRateMapper;
    @Autowired
    InvestMapper investMapper;
    @Autowired
    InvestService investService;

    @PostMapping("/invest")
    @ApiOperation("Api-投资")
    @ApiResponses({
            @ApiResponse(code = 2000, message = "INVEST NOT EXIST"),
            @ApiResponse(code = 2001, message = "INVEST_BALANCE_NOT_ENOUGH"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity invest(@RequestBody InvestInfoVo investInfoVo) {
        log.debug(" do invest action !!");
        Invest invest = investMapper.findByUserId(investInfoVo.getUserId());
        if(null == invest)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.INVEST_NOT_EXIST,null));

        BigDecimal investBalance = invest.getBalance().subtract(investInfoVo.getAmount()).setScale(Constants.KEEP_SCALE);
        if(BigDecimal.ZERO.compareTo(investBalance) >= 1)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.INVEST_BALANCE_NOT_ENOUGH,null));

        boolean result = investService.invest(invest,investInfoVo,investBalance);
        if(!result)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));

        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,null));
    }

}
