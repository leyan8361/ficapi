package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ExchangeRateVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.ExchangeRate;
import com.fic.service.mapper.ExchangeRateMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1")
@Api("Api-兑币比例")
public class ApiExchangeRateController {

    private final Logger log = LoggerFactory.getLogger(ApiExchangeRateController.class);

    @Autowired
    ExchangeRateMapper exchangeRateMapper;

    @GetMapping("/getFicRate")
    @ApiOperation("Api-获取FIC换算RMB费率")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = ExchangeRateVo.class)
    })
    public ResponseEntity getFICRate() throws InvocationTargetException, IllegalAccessException {
        log.debug(" do get fic rate action !!");
        ExchangeRate rate = exchangeRateMapper.findFicExchangeCny();
        if(null == rate)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.EXCHANGE_RATE_MISSED,null));
        ExchangeRateVo rateVo = new ExchangeRateVo();
        BeanUtils.copyProperties(rateVo,rate);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,rateVo));
    }
}
