package com.fic.service.controller;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetMovie;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/12/17
 *   @Discription:
**/
@RestController
@RequestMapping("/backend/betMovie")
@Api("竞猜管理")
public class BetMovieController {

    private final Logger log = LoggerFactory.getLogger(BetMovieController.class);

    @Autowired
    BetMovieService betMovieService;

    @GetMapping("/add")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "betMovieName", value = "竞猜电影名称"),
            @ApiImplicitParam(dataType = "date", name = "activityTime", value = "上架时间"),
            @ApiImplicitParam(dataType = "date", name = "disabledTime", value = "下架时间"),
            @ApiImplicitParam(dataType = "string", name = "movieType", value = "电影类型"),
            @ApiImplicitParam(dataType = "string", name = "movieDirector", value = "导演"),
            @ApiImplicitParam(dataType = "date", name = "showTime", value = "上映日期"),
    })
    @ApiOperation("新增项目 项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房) ")
    public ResponseEntity add(@RequestParam("betMovieName")String betMovieName,
                              @RequestParam("activityTime") Date activityTime,
                              @RequestParam("disabledTime") Date disabledTime,
                              @RequestParam("movieType") String movieType,
                              @RequestParam("movieDirector") String movieDirector,
                              @RequestParam("showTime") Date showTime,
                              @RequestParam("movieCoverFile")MultipartFile movieCoverFile
                              ) {
        log.debug(" bet add !!!");
        BetMovie betMovie = new BetMovie();
        betMovie.setBetMovieName(betMovieName);
        betMovie.setActivityTime(activityTime);
        betMovie.setDisabledTime(disabledTime);
        betMovie.setMovieDirector(movieDirector);
        betMovie.setMovieType(movieType);
        betMovie.setShowTime(showTime);
        ResponseVo result = betMovieService.add(betMovie,movieCoverFile);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/update")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "betMovieName", value = "竞猜电影名称"),
            @ApiImplicitParam(dataType = "date", name = "activityTime", value = "上架时间"),
            @ApiImplicitParam(dataType = "date", name = "disabledTime", value = "下架时间"),
            @ApiImplicitParam(dataType = "string", name = "movieType", value = "电影类型"),
            @ApiImplicitParam(dataType = "string", name = "movieDirector", value = "导演"),
            @ApiImplicitParam(dataType = "date", name = "showTime", value = "上映日期"),
    })
    @ApiOperation("更新项目 项目类型(0,单双）(1,能不能)(2, ABCD)(3,总票房) ")
    public ResponseEntity update(@RequestParam("betMovieName")String betMovieName,
                              @RequestParam("activityTime") Date activityTime,
                              @RequestParam("disabledTime") Date disabledTime,
                              @RequestParam("movieType") String movieType,
                              @RequestParam("movieDirector") String movieDirector,
                              @RequestParam("showTime") Date showTime,
                              @RequestParam("movieCoverFile")MultipartFile movieCoverFile
    ) {
        log.debug(" bet add !!!");
        BetMovie betMovie = new BetMovie();
        betMovie.setBetMovieName(betMovieName);
        betMovie.setActivityTime(activityTime);
        betMovie.setDisabledTime(disabledTime);
        betMovie.setMovieDirector(movieDirector);
        betMovie.setMovieType(movieType);
        betMovie.setShowTime(showTime);
        ResponseVo result = betMovieService.update(betMovie,movieCoverFile);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/onShelf")
    @ApiOperation("上架竞猜项目")
    public ResponseEntity onShelf() {
        log.debug(" bet on shelf !!!");

        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/shelf")
    @ApiOperation("下架竞猜项目")
    public ResponseEntity shelf() {
        log.debug(" bet shelf !!!");
        return ResponseEntity.ok().body("success");
    }

}
