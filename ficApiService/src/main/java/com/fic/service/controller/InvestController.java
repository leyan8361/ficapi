package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.RewardTypeEnum;
import com.fic.service.Vo.*;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.BalanceService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@RequestMapping("/backend")
@Api(description = "资产管理")
public class InvestController {

    private final Logger log = LoggerFactory.getLogger(InvestController.class);

    @Autowired
    UserMapper userMapper;
    @Autowired
    BalanceService balanceService;

    @PostMapping("/reward")
    @ApiOperation("新增奖励")
    @ApiResponses({
            @ApiResponse(code = 1001, message = "USER_NOT_EXIST"),
            @ApiResponse(code = 2000, message = "INVEST NOT EXIST"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestBody RewardInfoVo rewardInfoVo) {
        log.debug(" do post reward action backend!!");
        ResponseVo responseVo = balanceService.reward(rewardInfoVo);
        return ResponseEntity.ok(responseVo);
    }

    @GetMapping("/reward/{userId}")
    @ApiOperation("获取某个用户奖励")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "USER_NOT_EXIST"),
            @ApiResponse(code = 200, message = "SUCCESS",response = TradeRecordVo.class)
    })
    public ResponseEntity get(@RequestParam Integer userId) {
        log.debug(" do get reward action backend!!");
        List<RewardRecordInfoVo> resultList = balanceService.getReward(userId);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
    }

}
