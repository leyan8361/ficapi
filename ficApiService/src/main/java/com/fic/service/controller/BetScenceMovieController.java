package com.fic.service.controller;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetScenceMovie;
import com.fic.service.service.BetScenceMovieService;
import com.fic.service.service.BetScenceService;
import com.fic.service.utils.DateUtil;
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
import java.util.Date;


@RestController
@RequestMapping("/backend/bet/scence_movie")
@Api("竞猜场次管理")
public class BetScenceMovieController {

    private final Logger log = LoggerFactory.getLogger(BetScenceMovieController.class);

    @Autowired
    BetScenceMovieService betScenceMovieService;

    @Autowired
    BetScenceService betScenceService;

    @GetMapping("/add")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "scenceId", value = "项目ID"),
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "竞猜电影ID"),
            @ApiImplicitParam(dataType = "string", name = "startDay", value = "开始时间"),
            @ApiImplicitParam(dataType = "string", name = "endDay", value = "结束时间"),
            @ApiImplicitParam(dataType = "int", name = "hasJasckpot", value = "是否有奖池(0，无)(1，有)",required = true),
            @ApiImplicitParam(dataType = "BigDecimal", name = "jasckpotFee", value = "竞猜抽取手续费-->奖池(%)"),
            @ApiImplicitParam(dataType = "int", name = "hasReservation", value = "是否有备用金(0，无)(1，有)",required = true),
            @ApiImplicitParam(dataType = "BigDecimal", name = "reservationFee", value = "竞猜抽取手续费-->备用金(%)"),
    })
    @ApiOperation("设置场次")
    public ResponseEntity addMoive(@RequestParam("scenceId") int scenceId,
                                   @RequestParam("movieId") int movieId,
                                   @RequestParam("startDay") String startDay,
                                   @RequestParam("endDay") String endDay,
                                   @RequestParam("hasJasckpot") int hasJasckpot,
                                   @RequestParam("jasckpotFee") BigDecimal jasckpotFee,
                                   @RequestParam("hasReservation") int hasReservation,
                                   @RequestParam("reservationFee") BigDecimal reservationFee
    ) {
        log.debug(" add movie for scence !!!");
        BetScenceMovie scenceMovie = new BetScenceMovie();
        scenceMovie.setBetScenceId(scenceId);
        scenceMovie.setBetMovieId(movieId);
        scenceMovie.setStartDay(DateUtil.toMinFormatDay(startDay));
        scenceMovie.setEndDay(DateUtil.toMinFormatDay(endDay));
        scenceMovie.setHasJasckpot((byte)hasJasckpot);
        scenceMovie.setJasckpotFee(jasckpotFee);
        scenceMovie.setHasReservation((byte)hasReservation);
        scenceMovie.setReservationFee(reservationFee);
        ResponseVo result = betScenceMovieService.add(scenceMovie);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/update")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "场次ID",required = true),
            @ApiImplicitParam(dataType = "string", name = "startDay", value = "开始时间"),
            @ApiImplicitParam(dataType = "string", name = "endDay", value = "结束时间"),
            @ApiImplicitParam(dataType = "int", name = "hasJasckpot", value = "是否有奖池(0，无)(1，有)",required = true),
            @ApiImplicitParam(dataType = "BigDecimal", name = "jasckpotFee", value = "竞猜抽取手续费-->奖池(%)"),
            @ApiImplicitParam(dataType = "int", name = "hasReservation", value = "是否有备用金(0，无)(1，有)",required = true),
            @ApiImplicitParam(dataType = "BigDecimal", name = "reservationFee", value = "竞猜抽取手续费-->备用金(%)"),
    })
    @ApiOperation("设置场次")
    public ResponseEntity update(@RequestParam("id") int id,
                                   @RequestParam(value = "startDay",required = false) String startDay,
                                   @RequestParam(value = "endDay",required = false) String endDay,
                                   @RequestParam("hasJasckpot") int hasJasckpot,
                                   @RequestParam("jasckpotFee") BigDecimal jasckpotFee,
                                   @RequestParam("hasReservation") int hasReservation,
                                   @RequestParam("reservationFee") BigDecimal reservationFee
    ) {
        log.debug(" update movie_scence !!!");
        BetScenceMovie scenceMovie = new BetScenceMovie();
        scenceMovie.setId(id);
        scenceMovie.setStartDay(DateUtil.toMinFormatDay(startDay));
        scenceMovie.setEndDay(DateUtil.toMinFormatDay(endDay));
        scenceMovie.setHasJasckpot((byte)hasJasckpot);
        scenceMovie.setJasckpotFee(jasckpotFee);
        scenceMovie.setHasReservation((byte)hasReservation);
        scenceMovie.setReservationFee(reservationFee);
        ResponseVo result = betScenceMovieService.update(scenceMovie);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "场次ID"),
    })
    @ApiOperation("为项目删除电影")
    public ResponseEntity delete(@RequestParam("id") int id
    ) {
        log.debug(" delete movie for scence !!!");
        ResponseVo result = betScenceMovieService.delete(id);
        return ResponseEntity.ok(result);
    }

}
