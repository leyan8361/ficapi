package com.fic.service.controller.api;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.service.BetScenceService;
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

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/")
@Api("Api-竞猜")
public class ApiBetScenceController {

    private final Logger log = LoggerFactory.getLogger(ApiBetScenceController.class);

    @Autowired
    BetScenceService betScenceService;

    @GetMapping("/scence/getAllByBetType")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "betType", value = "项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房))"),
    })
    @ApiOperation("获取竞猜项目信息(包含当前项目涵盖的电影信息&票房信息")
    public ResponseEntity getAllByBetType(@RequestParam("betType")int betType) {
        log.debug("Api getScence!!!");
        ResponseVo result = betScenceService.getScence(betType);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/bet")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID",required = true),
            @ApiImplicitParam(dataType = "int", name = "scenceId", value = "项目ID",required = true),
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "下注电影ID",required = true),
            @ApiImplicitParam(dataType = "BigDecimal", name = "amount", value = "下注金额",required = true),
            @ApiImplicitParam(dataType = "string", name = "betWhich",required = true,
                    value = "下注类型(0,猜单双，单)(1,猜单双，双)(2,猜票房能不能，能)(3,猜票房能不能，不能)(4,选择题A)(5,选择题B)(6,选择题C)(7,选择题D)(当为高级场直接填写累计票房)"),
    })
    @ApiOperation("下注")
    public ResponseEntity bet(@RequestParam("userId")int userId,
                              @RequestParam("scenceId")int scenceId,
                              @RequestParam("movieId")int movieId,
                              @RequestParam("amount") BigDecimal amount,
                              @RequestParam("betWhich") String betWhich
    ) {
        log.debug("Api bet!!!");
        ResponseVo result = betScenceService.bet(userId,scenceId,movieId,amount,betWhich);
        return ResponseEntity.ok(result);
    }
}
