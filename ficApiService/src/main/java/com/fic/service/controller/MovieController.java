package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.BannerInfoVo;
import com.fic.service.Vo.MoiveAddInfoVo;
import com.fic.service.Vo.MovieInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.Movie;
import com.fic.service.mapper.MovieMapper;
import com.fic.service.service.MovieService;
import com.fic.service.utils.DateUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/backend/movie")
@Api(description = "电影管理")
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    MovieService movieService;

    @GetMapping("/getAll")
    @ApiOperation("获取所有")
    @ApiResponses({
            @ApiResponse(code = 4001, message = "BANNER_NOT_FOUND"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = Movie.class)
    })
    public ResponseEntity getAll() {
        log.debug(" movie getAll Action !!!");
        List<Movie> resultList = movieService.getAll();
        if(resultList.isEmpty()){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.MOVIE_NOT_FOUND,null));
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
    }

    @PostMapping(value = "/add")
    @ApiOperation("新增电影")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "movieName", value = "电影名称", required = true),
            @ApiImplicitParam(dataType = "BigDecimal", name = "budget", value = "总预算(亿)", required = true,example = "1.2"),
            @ApiImplicitParam(dataType = "BigDecimal", name = "quota", value = "开放额度(万)", required = true,example = "3000"),
            @ApiImplicitParam(dataType = "string", name = "showPlace", value = "上映地点", required = true,example = "中国香港"),
            @ApiImplicitParam(dataType = "string", name = "showTime", value = "上映时间", required = true,example = "2019-05-01"),
            @ApiImplicitParam(dataType = "string", name = "dutyDescription", value = "责任描述,以、号相隔", required = true,example = "承诺上映、完片担保"),
            @ApiImplicitParam(dataType = "string", name = "boxInfo", value = "票房", required = true,example = "1亿"),
            @ApiImplicitParam(dataType = "int", name = "investCycle", value = "周期(月)", required = true,example = "2"),
            @ApiImplicitParam(dataType = "int", name = "movieLast", value = "影片时长", required = true,example = "120"),
            @ApiImplicitParam(dataType = "int", name = "status", value = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)", required = true,example = "1"),
            @ApiImplicitParam(dataType = "BigDecimal", name = "returnRate", value = "回报率(%)", required = true,example = "125")
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestParam(name = "movieName",defaultValue = "电影名称")String movieName,
                              @RequestParam(name = "budget",defaultValue = "总预算(亿)")BigDecimal budget,
                              @RequestParam(name = "quota",defaultValue = "开放额度(万)")BigDecimal quota,
                              @RequestParam(name = "showPlace",defaultValue = "上映地点")String showPlace,
                              @RequestParam(name = "showTime",defaultValue = "上映地点")String showTime,
                              @RequestParam(name = "dutyDescription",defaultValue = "责任描述,以、号相隔")String dutyDescription,
                              @RequestParam(name = "boxInfo",defaultValue = "票房") String boxInfo,
                              @RequestParam(name = "investCycle",defaultValue = "周期(月)") int investCycle,
                              @RequestParam(name = "returnRate",defaultValue = "回报率(%)") BigDecimal returnRate,
                              @RequestParam(name = "movieLast",defaultValue = "影片时长") int movieLast,
                              @RequestParam(name = "status",defaultValue = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)") int status,
                              @RequestParam(name = "movieCoverFile",defaultValue = "电影封面") MultipartFile movieCoverFile
                              ) {
        log.debug(" movie add Action !!!");
        Movie movie = new Movie();
        movie.setMovieName(movieName);
        movie.setBudget(budget);
        movie.setQuota(quota);
        movie.setShowPlace(showPlace);
        movie.setShowTime(showTime);
        movie.setDutyDescription(dutyDescription);
        movie.setBoxInfo(boxInfo);
        movie.setInvestCycle(investCycle);
        movie.setReturnRate(returnRate);
        movie.setMovieLast(movieLast);
        movie.setStatus(status);
        movie.setCreatedTime(new Date());
        movie.setUpdatedTime(new Date());
        ResponseVo responseVo = movieService.add(movie,movieCoverFile);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "电影ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "movieName", value = "电影名"),
            @ApiImplicitParam(dataType = "BigDecimal", name = "budget", value = "总预算(亿)"),
            @ApiImplicitParam(dataType = "BigDecimal", name = "quota", value = "开放额度(万)"),
            @ApiImplicitParam(dataType = "string", name = "showPlace", value = "上映地点"),
            @ApiImplicitParam(dataType = "string", name = "showTime", value = "上映时间"),
            @ApiImplicitParam(dataType = "string", name = "dutyDescription", value = "责任描述,以、号相隔"),
            @ApiImplicitParam(dataType = "string", name = "boxInfo", value = "票房"),
            @ApiImplicitParam(dataType = "int", name = "investCycle", value = "周期(月)",example = "2"),
            @ApiImplicitParam(dataType = "int", name = "movieLast", value = "影片时长", example = "120"),
            @ApiImplicitParam(dataType = "int", name = "status", value = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)", example = "1"),
            @ApiImplicitParam(dataType = "BigDecimal", name = "returnRate", value = "回报率(%)", required = true,example = "125")
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(@RequestParam(name = "movieId",defaultValue = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)") Integer movieId,
                                 @RequestParam(name = "movieName",defaultValue = "电影名称")String movieName,
                                 @RequestParam(name = "budget",defaultValue = "总预算(亿)")BigDecimal budget,
                                 @RequestParam(name = "quota",defaultValue = "开放额度(万)")BigDecimal quota,
                                 @RequestParam(name = "showPlace",defaultValue = "上映地点")String showPlace,
                                 @RequestParam(name = "showTime",defaultValue = "上映地点")String showTime,
                                 @RequestParam(name = "dutyDescription",defaultValue = "责任描述,以、号相隔")String dutyDescription,
                                 @RequestParam(name = "boxInfo",defaultValue = "票房") String boxInfo,
                                 @RequestParam(name = "investCycle",defaultValue = "周期(月)") Integer investCycle,
                                 @RequestParam(name = "returnRate",defaultValue = "回报率(%)") BigDecimal returnRate,
                                 @RequestParam(name = "movieLast",defaultValue = "影片时长") int movieLast,
                                 @RequestParam(name = "status",defaultValue = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)") Integer status,
                                 @RequestParam(name = "movieCoverFile",defaultValue = "电影封面") MultipartFile movieCoverFile){
        log.debug(" movie update Action !!!");
        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setMovieName(movieName);
        movie.setBudget(budget);
        movie.setQuota(quota);
        movie.setShowPlace(showPlace);
        movie.setShowTime(showTime);
        movie.setDutyDescription(dutyDescription);
        movie.setBoxInfo(boxInfo);
        movie.setInvestCycle(investCycle);
        movie.setReturnRate(returnRate);
        movie.setMovieLast(movieLast);
        movie.setStatus(status);
        ResponseVo responseVo = movieService.update(movie,movieCoverFile);
        return ResponseEntity.ok(responseVo);
    }

//    @GetMapping("/onShelf")
//    @ApiOperation("上架")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "int", name = "id", value = "movie ID", required = true)
//    })
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "SUCCESS")
//    })
//    public ResponseEntity onShelf(@RequestParam Integer id) {
//        log.debug(" movie onShelf Action !!!");
//        ResponseVo responseVo = movieService.onShelf(id);
//        return ResponseEntity.ok(responseVo);
//    }
//
//
//    @GetMapping("/shelf")
//    @ApiOperation("下架")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "int", name = "id", value = "movie ID", required = true)
//    })
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "SUCCESS")
//    })
//    public ResponseEntity shelf(@RequestParam Integer id) {
//        log.debug(" movie shelf Action !!!");
//        ResponseVo responseVo = movieService.shelf(id);
//        return ResponseEntity.ok(responseVo);
//    }
}
