package com.fic.service.controller;

import com.fic.service.Vo.BetMovieVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetMovie;
import com.fic.service.service.BetMovieService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/12/17
 *   @Discription:
**/
@RestController
@RequestMapping("/backend/betMovie")
@Api("竞猜电影管理")
public class BetMovieController {

    private final Logger log = LoggerFactory.getLogger(BetMovieController.class);

    @Autowired
    BetMovieService betMovieService;


    @GetMapping("/getAll")
    @ApiOperation("查看所有")
    public ResponseEntity getAll() {
        log.debug(" bet getAll!!!");
        ResponseVo result =  betMovieService.getAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "betMovieName", value = "竞猜电影名称"),
//            @ApiImplicitParam(dataType = "date", name = "activityTime", value = "上架时间"),
//            @ApiImplicitParam(dataType = "date", name = "disabledTime", value = "下架时间"),
            @ApiImplicitParam(dataType = "string", name = "movieType", value = "电影类型"),
            @ApiImplicitParam(dataType = "string", name = "movieDirector", value = "导演"),
            @ApiImplicitParam(dataType = "date", name = "showTime", value = "上映日期"),
    })
    @ApiOperation("新增竞猜电影")
    public ResponseEntity add(
            @RequestParam("betMovieName")String betMovieName,
//                              @RequestParam("activityTime") String activityTime,
//                              @RequestParam("disabledTime") String disabledTime,
                              @RequestParam("movieType") String movieType,
                              @RequestParam("movieDirector") String movieDirector,
                              @RequestParam("showTime") String showTime,
                              @ApiParam("movieCoverFile")MultipartFile movieCoverFile
                              ) {
        log.debug(" bet add !!!");
        BetMovie betMovie = new BetMovie();
        betMovie.setBetMovieName(betMovieName);
//        betMovie.setActivityTime(DateUtil.toMinFormatDay(activityTime));
//        betMovie.setDisabledTime(DateUtil.toMinFormatDay(disabledTime));
        betMovie.setMovieDirector(movieDirector);
        betMovie.setMovieType(movieType);
        betMovie.setShowTime(DateUtil.toDayFormatDay(showTime));
        ResponseVo result = betMovieService.add(betMovie,movieCoverFile);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/update")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "betMovieName", value = "竞猜电影名称"),
//            @ApiImplicitParam(dataType = "date", name = "activityTime", value = "上架时间"),
//            @ApiImplicitParam(dataType = "date", name = "disabledTime", value = "下架时间"),
            @ApiImplicitParam(dataType = "string", name = "movieType", value = "电影类型"),
            @ApiImplicitParam(dataType = "string", name = "movieDirector", value = "导演"),
            @ApiImplicitParam(dataType = "date", name = "showTime", value = "上映日期"),
    })
    @ApiOperation("更新竞猜电影")
    public ResponseEntity update(@RequestParam("betMovieName")String betMovieName,
//                              @RequestParam("activityTime") Date activityTime,
//                              @RequestParam("disabledTime") Date disabledTime,
                              @RequestParam("movieType") String movieType,
                              @RequestParam("movieDirector") String movieDirector,
                              @RequestParam("showTime") Date showTime,
                              @RequestParam("movieCoverFile")MultipartFile movieCoverFile
    ) {
        log.debug(" bet add !!!");
        BetMovie betMovie = new BetMovie();
        betMovie.setBetMovieName(betMovieName);
//        betMovie.setActivityTime(activityTime);
//        betMovie.setDisabledTime(disabledTime);
        betMovie.setMovieDirector(movieDirector);
        betMovie.setMovieType(movieType);
        betMovie.setShowTime(showTime);
        ResponseVo result = betMovieService.update(betMovie,movieCoverFile);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/onShelf")
    @ApiOperation("上架竞猜项目")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "电影ID")
    })
    public ResponseEntity onShelf(int id) {
        log.debug(" bet on shelf !!!");
        ResponseVo result =  betMovieService.onShelf(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/shelf")
    @ApiOperation("下架竞猜项目")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "电影ID")
    })
    public ResponseEntity shelf(int id) {
        log.debug(" bet shelf !!!");
        ResponseVo result =  betMovieService.shelf(id);
        return ResponseEntity.ok(result);
    }



}
