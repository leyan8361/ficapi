package com.fic.service.controller.api.v1;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.Vo.TradeRecordInfoVo;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.BalanceService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *   @Author Xie
 *   @Date 2018/12/5
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1")
@Api(description = "Api-资产变动记录相关")
public class ApiMoneyController {

    private final Logger log = LoggerFactory.getLogger(ApiMoneyController.class);

    @Autowired
    BalanceService balanceService;
    @Autowired
    UserMapper userMapper;


    @GetMapping("/tradeRecord")
    @ApiOperation("Api-交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = TradeRecordInfoVo.class)
    })
    public ResponseEntity tradeRecord(@RequestParam Integer userId) {
        log.debug(" Api tradeRecord Action !!!");
        TradeRecordInfoVo result = balanceService.getTradeRecord(userId);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }
}
