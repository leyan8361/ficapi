package com.fic.service.controller.api;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.service.BetMovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@RestController
@RequestMapping("/api/v1/betMovie")
@Api("Api-获取竞猜电影信息")
public class ApiBetMovieController {

    private final Logger log = LoggerFactory.getLogger(ApiBetMovieController.class);

    @Autowired
    BetMovieService betMovieService;

    @GetMapping("/getHistory")
    @ApiImplicitParams({
    })
    @ApiOperation("获取竞猜电影列表 (开奖了的)")
    public ResponseEntity getHistory() {
        log.debug("Api bet get All History !!!");
        ResponseVo result = betMovieService.getHistory();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAll")
    @ApiImplicitParams({
    })
    @ApiOperation("获取竞猜电影列表 (未开奖的)")
    public ResponseEntity getAll() {
        log.debug("Api bet get All !!!");
        ResponseVo result = betMovieService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getById")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "betMovieId", value = "竞猜电影ID"),
    })
    @ApiOperation("获取竞猜电影详情")
    public ResponseEntity getById(@RequestParam("betMovieId")int betMovieId) {
        log.debug("Api bet get by id!!!");
        ResponseVo result = betMovieService.getById(betMovieId);
        return ResponseEntity.ok(result);
    }
}
