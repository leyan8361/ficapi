package com.fic.service.controller.api;

import com.fic.service.Vo.BetMovieDrawVo;
import com.fic.service.Vo.BetMovieInfoVo;
import com.fic.service.Vo.LoginUserInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.service.BetMovieService;
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

//    @GetMapping("/getMovieOn")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "int", name = "betType", value = "项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房))"),
//    })
//    @ApiResponses({
//            @ApiResponse(code = 5008, message = "THE BET TYPE HAS NO SCENCE"),
//            @ApiResponse(code = 200, message = "SUCCESS",response = BetMovieInfoVo.class)
//    })
//    @ApiOperation("获取竞猜电影列表 (未开奖的)")
//    public ResponseEntity getHistory(@RequestParam("betType")int betType) {
//        log.debug("Api bet get movie on !!!");
//        ResponseVo result = betMovieService.getMovieOn(betType);
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/getHistory")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "int", name = "betType", value = "项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房))"),
//    })
//    @ApiResponses({
//            @ApiResponse(code = 5008, message = "THE BET TYPE HAS NO SCENCE"),
//            @ApiResponse(code = 200, message = "SUCCESS",response = BetMovieDrawVo.class)
//    })
//    @ApiOperation("获取竞猜电影列表 (开奖了的)")
//    public ResponseEntity getAll(@RequestParam("betType")int betType) {
//        log.debug("Api bet getHistory !!!");
//        ResponseVo result = betMovieService.getHistory(betType);
//        return ResponseEntity.ok(result);
//    }

//    @GetMapping("/getById")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "int", name = "scenceMovieId", value = "场次ID"),
//    })
//    @ApiOperation("获取竞猜场次详情")
//    public ResponseEntity getById(@RequestParam("betMovieId")int betMovieId) {
//        log.debug("Api bet get by id!!!");
//        ResponseVo result = betMovieService.getById(betMovieId);
//        return ResponseEntity.ok(result);
//    }
}
