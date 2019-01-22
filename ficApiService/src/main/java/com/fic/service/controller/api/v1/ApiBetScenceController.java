package com.fic.service.controller.api.v1;
import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.*;
import com.fic.service.service.BetScenceService;
import com.fic.service.utils.DateUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/")
@Api(description = "Api-竞猜")
public class ApiBetScenceController {

    private final Logger log = LoggerFactory.getLogger(ApiBetScenceController.class);

    @Autowired
    BetScenceService betScenceService;

    @GetMapping("/scence/getAllByBetType")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "betType", value = "项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房))"),
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = BetInfoVo.class)
    })
    @ApiOperation("获取竞猜项目列表信息(包含当前项目涵盖的电影信息&票房信息")
    public ResponseEntity getAllByBetType(@RequestParam("betType") int betType, @RequestParam("userId") int userId) {
        log.debug("Api getScence!!!");
        ResponseVo result = betScenceService.getScence(betType, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/scence/getByScenceMovieId")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "scenceMovieId", value = "场次ID"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = BetMovieInfoVo.class)
    })
    @ApiOperation("获取某个场次信息")
    public ResponseEntity getByScenceMovieId(@RequestParam("scenceMovieId") int scenceMovieId) {
        log.debug("Api get scence movie by ID!!!");
        ResponseVo result = betScenceService.getScenceMovie(scenceMovieId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/bet")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "int", name = "scenceMovieId", value = "场次ID", required = true),
            @ApiImplicitParam(dataType = "double", name = "amount", value = "下注金额", required = true),
            @ApiImplicitParam(dataType = "string", name = "betWhich", required = true,
                    value = "下注类型(0,猜单双，单)(1,猜单双，双)(2,猜票房能不能，能)(3,猜票房能不能，不能)(4,选择题A)(5,选择题B)(6,选择题C)(7,选择题D)(当为高级场直接填写累计票房)"),
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "User Not Exist"),
            @ApiResponse(code = 2000, message = "INVEST NOT EXIST"),
            @ApiResponse(code = 2001, message = "INVEST_BALANCE_NOT_ENOUGH"),
            @ApiResponse(code = 5006, message = "SCENCE_MOVIE_NOT_EXIST"),
            @ApiResponse(code = 5004, message = "NO_AVALIBLE_SCENCE"),
            @ApiResponse(code = 5002, message = "NO COULD USED BET MOVIE"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 5010, message = "BET_TIME_LOCK"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    @ApiOperation("下注")
    public ResponseEntity bet(@RequestParam("userId") int userId,
                              @RequestParam("scenceMovieId") int scenceMovieId,
                              @RequestParam("amount") BigDecimal amount,
                              @RequestParam("betWhich") String betWhich
    ) {
        log.debug("Api bet!!!");
        if (DateUtil.betLockTime()) {
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.BET_TIME_LOCK, null));
        }
        ResponseVo result = betScenceService.bet(userId, scenceMovieId, amount, betWhich);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getMyBetRecord")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 5009, message = "NO_BET_RECORD"),
            @ApiResponse(code = 200, message = "SUCCESS", response = BetRecordInfoVo.class)
    })
    @ApiOperation("获取我的竞猜记录")
    public ResponseEntity getMyBetRecord(@RequestParam("userId") int userId
    ) {
        log.debug("Api get bet record !!!");
        ResponseVo result = betScenceService.getMyBetRecord(userId);
        return ResponseEntity.ok(result);
    }

//    @GetMapping("/getBetRanking")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "SUCCESS", response = BetRecordInfoVo.class)
//    })
//    @ApiOperation("getBetRanking")
//    public ResponseEntity getBetRanking() {
//        log.debug("Api get bet record !!!");
//        ResponseVo result = betScenceService.getBetRanking();
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/getSignData")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = BetSignVo.class)
    })
    @ApiOperation("获取签到记录")
    public ResponseEntity getSignData(@RequestParam("userId") int userId) {
        log.debug("Api get bet record !!!");
        ResponseVo result = betScenceService.getSignData(userId);
        return ResponseEntity.ok(result);
    }
}