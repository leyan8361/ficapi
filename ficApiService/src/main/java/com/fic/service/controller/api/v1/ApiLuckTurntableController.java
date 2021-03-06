package com.fic.service.controller.api.v1;

import com.fic.service.Vo.*;
import com.fic.service.service.LuckTurntableService;
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
 *   @Date 2019/1/17
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1/luckTurntable")
@Api(description = "Api-转盘")
public class ApiLuckTurntableController {

    private final Logger log = LoggerFactory.getLogger(ApiLuckTurntableController.class);

    @Autowired
    LuckTurntableService luckTurntableService;

    @GetMapping("/getData")
    @ApiOperation("Api-获取转盘奖品、概率、封面等数据")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = LuckTurntableInfoVo.class)
    })
    public ResponseEntity getData(int userId){
        log.debug(" do getPrice action !!");
        ResponseVo result = luckTurntableService.getPrice(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/draw")
    @ApiOperation("Api-记录抽奖结果")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "int", name = "priceId", value = "奖品ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "word", value = "中奖金句时传入金句", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = LuckyDrawResultVo.class)
    })
    public ResponseEntity draw(@RequestParam Integer userId,@RequestParam Integer priceId,@RequestParam(required = false) String word){
        log.debug(" do draw action !!");
        ResponseVo result = luckTurntableService.draw(userId,priceId,word);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getBingoRecord")
    @ApiOperation("Api-获取中奖记录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "int", name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(dataType = "int", name = "type", value = "(0,全部)(1,未领取)(2,已领取)", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = LuckTurntableRecordVo.class)
    })
    public ResponseEntity getBingoRecord(@RequestParam int userId,@RequestParam int pageNum,@RequestParam int type){
        log.debug(" do getBingoRecord action !!");
        ResponseVo result = luckTurntableService.getBingoRecord(userId,pageNum,type);
        return ResponseEntity.ok(result);
    }
}
