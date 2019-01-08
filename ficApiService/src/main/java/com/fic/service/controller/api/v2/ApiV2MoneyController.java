package com.fic.service.controller.api.v2;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.Vo.TradeRecordInfoV2Vo;
import com.fic.service.Vo.TradeRecordInfoVo;
import com.fic.service.Vo.TradeRecordRequestVo;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.BalanceService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *   @Author Xie
 *   @Date 2018/12/5
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v2")
@Api(description = "Api-资产变动记录相关")
public class ApiV2MoneyController {

    private final Logger log = LoggerFactory.getLogger(ApiV2MoneyController.class);

    @Autowired
    BalanceService balanceService;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/tradeRecord")
    @ApiOperation("Api-交易记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = TradeRecordInfoV2Vo.class)
    })
    public ResponseEntity tradeRecord(@RequestBody TradeRecordRequestVo recordInfoV2Vo) {
        log.debug(" Api tradeRecord V2 Action !!!");
        ResponseVo result = balanceService.getTradeRecordV2(recordInfoV2Vo);
        return ResponseEntity.ok(result);
    }


}
