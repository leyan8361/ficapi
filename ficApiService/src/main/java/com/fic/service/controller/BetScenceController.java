package com.fic.service.controller;

import com.fic.service.Vo.BetScenceVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetScence;
import com.fic.service.entity.BetScenceMovie;
import com.fic.service.scheduled.BetScheduledService;
import com.fic.service.service.BetScenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/12/17
 *   @Discription:
**/
@RestController
@RequestMapping("/backend/bet/scence")
@Api(description = "竞猜项目")
public class BetScenceController {

    private final Logger log = LoggerFactory.getLogger(BetScenceController.class);

    @Autowired
    BetScenceService betScenceService;
    @Autowired
    BetScheduledService betScheduledService;

    @GetMapping("/getAll")
    @ApiOperation("查看所有项目")
    public ResponseEntity getAll() {
        log.debug(" bet get All !!!");
        ResponseVo result = betScenceService.getAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    @ApiOperation("新增项目 项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房) ")
    public ResponseEntity add(@RequestBody BetScenceVo betScenceVo) {
        log.debug(" bet add !!!");
        ResponseVo result = betScenceService.add(betScenceVo);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/update")
    @ApiOperation("修改项目 项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房) ")
    public ResponseEntity update(@RequestBody BetScenceVo betScenceVo) {
        log.debug(" bet update !!!");
        ResponseVo result = betScenceService.add(betScenceVo);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recharge")
    @ApiOperation("充值备用金")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "double", name = "amount", value = "充值金额")
    })
    public ResponseEntity recharge(@RequestParam BigDecimal amount) {
        System.out.println("recharge !!!!!");
        betScheduledService.recharge(amount);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/returning")
    @ApiOperation("人工赔付")
    public ResponseEntity returning() {
        System.out.println("returning !!!!!");
        betScheduledService.makeUpReturning();
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/reward")
    @ApiOperation("人工分奖池")
    public ResponseEntity pull() {
        System.out.println("reward !!!!!");
        betScheduledService.rewardPool();
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/doOpenPrice")
    @ApiOperation("人工开奖")
    public ResponseEntity doOpenPrice() {
        System.out.println("doOpenPrice !!!!!");
        betScheduledService.doBoxPull();//拉数据
        betScheduledService.openPrice();
        return ResponseEntity.ok().body("success");
    }

//    @GetMapping("/onShelf")
//    @ApiOperation("上架竞猜项目")
//    public ResponseEntity onShelf() {
//        log.debug(" bet on shelf !!!");
//
//        return ResponseEntity.ok().body("success");
//    }
//
//    @GetMapping("/shelf")
//    @ApiOperation("下架竞猜项目")
//    public ResponseEntity shelf() {
//        log.debug(" bet shelf !!!");
//        return ResponseEntity.ok().body("success");
//    }
}
