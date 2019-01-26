package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.ExchangeRate;
import com.fic.service.mapper.ExchangeRateMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/rate")
@Api(description = "币率换算")
public class ExchangeRateController {

    private final Logger log = LoggerFactory.getLogger(ExchangeRateController.class);

    @Autowired
    ExchangeRateMapper exchangeRateMapper;

    @GetMapping("/getAll")
    @ApiOperation("查看所有")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = ExchangeRate.class)
    })
    public ResponseEntity getAll() {
        log.debug(" get all action !!!");
        List<ExchangeRate> result = exchangeRateMapper.findAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    @ApiOperation("增加新汇率")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestBody ExchangeRate exchangeRate) {
        log.debug(" add action !!!");
        int checkResult = exchangeRateMapper.findIfExistSame(exchangeRate.getCoin1(),exchangeRate.getCoin2());
        if(checkResult > 0){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.EXCHANGE_RATE_REPEATED,null));
        }
        int saveResult = exchangeRateMapper.insertSelective(exchangeRate);
        if(saveResult <=0){
            log.error("增加新汇率失败");
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    @ApiOperation("更新汇率")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(@RequestBody ExchangeRate exchangeRate) {
        log.debug(" update action !!!");
        int saveResult = exchangeRateMapper.updateByPrimaryKey(exchangeRate);
        if(saveResult <=0){
            log.error("增加新汇率失败");
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除新汇率")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity delete(@RequestParam int id) {
        log.debug(" delete action !!!");
        ExchangeRate rate = exchangeRateMapper.selectByPrimaryKey(id);
        if(null == rate){
            log.debug(" rate not exist id :{}",id);
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.EXCHANGE_RATE_MISSED,null));
        }
        int deleteResult = exchangeRateMapper.deleteByPrimaryKey(id);
        if(deleteResult <=0){
            log.debug("delete rate failed");
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null));
        }
        return ResponseEntity.ok().build();
    }
}
