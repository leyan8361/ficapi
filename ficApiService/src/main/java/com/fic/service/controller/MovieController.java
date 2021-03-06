package com.fic.service.controller;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.Movie;
import com.fic.service.service.MovieService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;

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
        ResponseVo result = movieService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllActorInfoByMovie")
    @ApiOperation("查看某个电影的所有演员")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "电影ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = Movie.class)
    })
    public ResponseEntity getAllActorInfo(@RequestParam int movieId) {
        log.debug(" getAllActorInfo Action !!!");
        ResponseVo result = movieService.getAllActorInfoByMovie(movieId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllMovieDetailInfoByMovie")
    @ApiOperation("查看某个电影的项目简介")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "电影ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = Movie.class)
    })
    public ResponseEntity getAllMovieDetailInfoByMovie(int movieId) {
        log.debug(" getAllMovieDetailInfoByMovie Action !!!");
        ResponseVo result = movieService.getAllMovieDetailInfoByMovie(movieId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/add")
    @ApiOperation("新增电影")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "movieName", value = "电影名称", required = true),
            @ApiImplicitParam(dataType = "string", name = "movieType", value = "电影类型", required = true),
            @ApiImplicitParam(dataType = "double", name = "budget", value = "总预算(亿)", required = true,example = "1.2"),
            @ApiImplicitParam(dataType = "double", name = "quota", value = "开放额度(万)", required = true,example = "3000"),
            @ApiImplicitParam(dataType = "string", name = "showPlace", value = "上映地点", required = true,example = "中国香港"),
            @ApiImplicitParam(dataType = "string", name = "showTime", value = "上映时间", required = true,example = "2019-05-01"),
            @ApiImplicitParam(dataType = "string", name = "dutyDescription", value = "责任描述,以、号相隔", required = true,example = "承诺上映、完片担保"),
            @ApiImplicitParam(dataType = "string", name = "boxInfo", value = "票房", required = true,example = "1亿"),
            @ApiImplicitParam(dataType = "int", name = "investCycle", value = "周期(月)", required = true,example = "2"),
            @ApiImplicitParam(dataType = "int", name = "movieLast", value = "影片时长", required = true,example = "120"),
            @ApiImplicitParam(dataType = "int", name = "status", value = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)", required = true,example = "1"),
            @ApiImplicitParam(dataType = "double", name = "sort", value = "排序，小的在前面", required = true,example = "0.1 or 1"),
            @ApiImplicitParam(dataType = "double", name = "returnRate", value = "回报率(%)", required = true,example = "125"),
            @ApiImplicitParam(dataType = "int", name = "investCount", value = "参投人数,数字", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestParam(name = "movieName",defaultValue = "电影名称")String movieName,
                              @RequestParam(name = "movieType",defaultValue = "电影类型")String movieType,
                              @RequestParam(name = "budget",defaultValue = "总预算(亿)")BigDecimal budget,
                              @RequestParam(name = "quota",defaultValue = "开放额度(万)")BigDecimal quota,
                              @RequestParam(name = "showPlace",defaultValue = "上映地点")String showPlace,
                              @RequestParam(name = "showTime",defaultValue = "上映时间")String showTime,
                              @RequestParam(name = "dutyDescription",defaultValue = "责任描述,以、号相隔")String dutyDescription,
                              @RequestParam(name = "boxInfo",defaultValue = "票房") String boxInfo,
                              @RequestParam(name = "investCycle",defaultValue = "周期(月)") int investCycle,
                              @RequestParam(name = "returnRate",defaultValue = "回报率(%)") BigDecimal returnRate,
                              @RequestParam(name = "movieLast",defaultValue = "影片时长") Integer movieLast,
                              @RequestParam(name = "status",defaultValue = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)") int status,
                              @RequestParam(name = "sort",defaultValue = "排序，小的在前面") BigDecimal sort,
                              @RequestParam(name = "investCount",defaultValue = "参投人数") Integer investCount,
                              @RequestParam(name = "movieCoverFile",defaultValue = "电影封面") MultipartFile movieCoverFile
                              ) {
        log.debug(" movie add Action !!!");
        Movie movie = new Movie();
        movie.setMovieName(movieName);
        movie.setMovieType(movieType);
        movie.setShowPlace(showPlace);
        movie.setShowTime(showTime);
        movie.setDutyDescription(dutyDescription);
        movie.setBoxInfo(boxInfo);
        movie.setInvestCycle(investCycle);
        if(null != budget && budget.compareTo(BigDecimal.ZERO) >0){
            movie.setBudget(budget);
        }
        if(null !=quota && quota.compareTo(BigDecimal.ZERO) >0){
            movie.setQuota(quota);
        }
//        if(null != returnRate && returnRate.compareTo(BigDecimal.ZERO)>0){
//            movie.setReturnRate(returnRate);
//        }
        if(null != movieLast && 0!=movieLast){
            movie.setMovieLast(movieLast);
        }
        movie.setSort(sort);
        movie.setReturnRate(returnRate);
        movie.setStatus(status);
        movie.setInvestCount(investCount);
        movie.setCreatedTime(new Date());
        movie.setUpdatedTime(new Date());
        ResponseVo responseVo = movieService.add(movie,movieCoverFile);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping(value = "/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiOperation("修改")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "电影ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "movieName", value = "电影名"),
            @ApiImplicitParam(dataType = "string", name = "movieType", value = "电影类型"),
            @ApiImplicitParam(dataType = "double", name = "budget", value = "总预算(亿)"),
            @ApiImplicitParam(dataType = "double", name = "quota", value = "开放额度(万)"),
            @ApiImplicitParam(dataType = "string", name = "showPlace", value = "上映地点"),
            @ApiImplicitParam(dataType = "string", name = "showTime", value = "上映时间"),
            @ApiImplicitParam(dataType = "string", name = "dutyDescription", value = "责任描述,以、号相隔"),
            @ApiImplicitParam(dataType = "string", name = "boxInfo", value = "票房"),
            @ApiImplicitParam(dataType = "int", name = "investCycle", value = "周期(月)"),
            @ApiImplicitParam(dataType = "int", name = "movieLast", value = "影片时长"),
            @ApiImplicitParam(dataType = "int", name = "status", value = "(0，已杀青)(1，待开机)(2,已分红)(3,待分红)"),
            @ApiImplicitParam(dataType = "double", name = "sort", value = "排序，小的在前面", example = "0.1 or 1"),
            @ApiImplicitParam(dataType = "double", name = "returnRate", value = "回报率(%)"),
            @ApiImplicitParam(dataType = "int", name = "investCount", value = "参投人数,数字")
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(@RequestParam Integer movieId,
                                 @RequestParam(required = false)String movieName,
                                 @RequestParam(required = false)String movieType,
                                 @RequestParam(required = false)BigDecimal budget,
                                 @RequestParam(required = false)BigDecimal quota,
                                 @RequestParam(required = false)String showPlace,
                                 @RequestParam(required = false)String showTime,
                                 @RequestParam(required = false)String dutyDescription,
                                 @RequestParam(required = false) String boxInfo,
                                 @RequestParam(required = false) Integer investCycle,
                                 @RequestParam(required = false) BigDecimal returnRate,
                                 @RequestParam(required = false) Integer movieLast,
                                 @RequestParam(required = false) Integer status,
                                 @RequestParam(required = false) BigDecimal sort,
                                 @RequestParam(required = false) Integer investCount,
                                 @ApiParam MultipartFile movieCoverFile){
        log.debug(" movie update Action !!!");
        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setMovieName(movieName);
        movie.setMovieType(movieType);

        movie.setShowPlace(showPlace);
        movie.setShowTime(showTime);
        movie.setDutyDescription(dutyDescription);
        movie.setBoxInfo(boxInfo);
        movie.setInvestCycle(investCycle);
        if(null != budget && budget.compareTo(BigDecimal.ZERO) >0){
            movie.setBudget(budget);
        }
        if(null !=quota && quota.compareTo(BigDecimal.ZERO) >0){
            movie.setQuota(quota);
        }
//        if(null != returnRate && returnRate.compareTo(BigDecimal.ZERO)>0){
//            movie.setReturnRate(returnRate);
//        }
        if(null != movieLast && 0!=movieLast){
            movie.setMovieLast(movieLast);
        }
        movie.setInvestCount(investCount);
        movie.setSort(sort);
        movie.setReturnRate(returnRate);
        movie.setStatus(status);
        movie.setUpdatedTime(new Date());
        ResponseVo responseVo = movieService.update(movie,movieCoverFile);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping(value = "/addActorInfo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiOperation("新增演员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "movie ID", required = true,example = "1"),
            @ApiImplicitParam(dataType = "string", name = "role", value = "角色", required = true,example = "导演"),
            @ApiImplicitParam(dataType = "string", name = "roleName", value = "角色名称", required = true,example = "温子仁")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity addActorInfo(@RequestParam int id,
                                       @RequestParam String role,
                                       @RequestParam String roleName,
                                       @ApiParam(required = true) MultipartFile movieCoverFile) {
        log.debug(" add actor info Action !!!");
        ResponseVo responseVo = movieService.addActorInfo(id,role,roleName,movieCoverFile);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping(value = "/updateActorInfo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiOperation("更新演员")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "actorId", value = "演员ID", required = true,example = "1"),
            @ApiImplicitParam(dataType = "string", name = "role", value = "角色", required = false,example = "导演"),
            @ApiImplicitParam(dataType = "string", name = "roleName", value = "角色名称", required = false,example = "温子仁")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity updateActorInfo(
                                       @RequestParam int actorId,
                                       @RequestParam(required = false) String role,
                                       @RequestParam(required = false) String roleName,
                                       @ApiParam(required = false) MultipartFile movieCoverFile) {
        log.debug(" update actor info Action !!!");
        ResponseVo responseVo = movieService.updateActorInfo(actorId,role,roleName,movieCoverFile);
        return ResponseEntity.ok(responseVo);
    }

    @DeleteMapping(value = "/deleteBrief")
    @ApiOperation("删除项目简介")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "briefId", value = "项目简介 ID", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity deleteBrief(
            @RequestParam int briefId) {
        log.debug(" delete brief Action !!!");
        ResponseVo responseVo = movieService.deleteBrief(briefId);
        return ResponseEntity.ok(responseVo);
    }

    @DeleteMapping(value = "/deleteActorInfo")
    @ApiOperation("删除演员")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "actorId", value = "演员ID", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity deleteActorInfo(
            @RequestParam int actorId) {
        log.debug(" delete actor info Action !!!");
        ResponseVo responseVo = movieService.deleteActorInfo(actorId);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping(value = "/addBrief",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiOperation("新增项目简介")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "movie ID", required = true,example = "1"),
            @ApiImplicitParam(dataType = "text", name = "briefText", value = "项目简介文本", required = true),
            @ApiImplicitParam(dataType = "text", name = "plotSummary", value = "剧情简介文本", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity addActorInfo(@RequestParam int id,
                                       @RequestParam String briefText,
                                       @RequestParam String plotSummary,
                                       @ApiParam(name ="briefCoverFile",value = "项目简介图",required = true) MultipartFile briefCoverFile,
                                       @ApiParam(name ="plotSummaryCoverFile",value = "剧情简介图",required = true) MultipartFile plotSummaryCoverFile) {
        log.debug(" add Brief Action !!!");
        ResponseVo responseVo = movieService.addBrief(id,briefText,plotSummary,briefCoverFile,plotSummaryCoverFile);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping(value = "/updateBrief",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiOperation("更新项目简介")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "briefId", value = "项目简介 ID", required = true,example = "1"),
            @ApiImplicitParam(dataType = "text", name = "briefText", value = "项目简介文本", required = false),
            @ApiImplicitParam(dataType = "text", name = "plotSummary", value = "剧情简介文本", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity updateBrief(@RequestParam int briefId,
                                       @RequestParam(required = false) String briefText,
                                       @RequestParam(required = false) String plotSummary,
                                       @ApiParam(name ="briefCoverFile",value = "项目简介图",required = false) MultipartFile briefCoverFile,
                                       @ApiParam(name ="plotSummaryCoverFile",value = "剧情简介图",required = false) MultipartFile plotSummaryCoverFile) {
        log.debug(" update Brief Action !!!");
        ResponseVo responseVo = movieService.updateBrief(briefId,briefText,plotSummary,briefCoverFile,plotSummaryCoverFile);
        return ResponseEntity.ok(responseVo);
    }
}
