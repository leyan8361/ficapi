package com.fic.service.controller;

import com.fic.service.Vo.OmLuckyRecordVo;
import com.fic.service.Vo.ResponseVo;
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
 *   @Date 2019/1/23
 *   @Discription:
**/
@RestController
@RequestMapping("/backend/lucky")
@Api(description = "抽奖结果管理")
public class LuckyRecordController {

    private final Logger log = LoggerFactory.getLogger(LuckyRecordController.class);

    @Autowired
    LuckTurntableService luckTurntableService;

    @GetMapping("/getLuckRecord")
    @ApiOperation("查看用户抽奖数据 (0,查看所有)(1,待审批)(2,已审批)")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "condition", value = " (0,查看所有)(1,待审批)(2,已审批)", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = OmLuckyRecordVo.class)
    })
    public ResponseEntity getLuckRecord(@RequestParam int condition) {
        log.debug(" lucky getLuckRecord!!!");
        ResponseVo result = luckTurntableService.getLuckRecord(condition);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/approveReceive")
    @ApiOperation("修改用户抽奖已领取")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "recordId", value = "用户抽奖记录ID", required = true)
    })
    public ResponseEntity approveReceive(@RequestParam int recordId) {
        log.debug(" lucky approveReceive!!!");
        ResponseVo result = luckTurntableService.approveReceive(recordId);
        return ResponseEntity.ok(result);
    }
}
