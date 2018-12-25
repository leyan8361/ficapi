package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.BannerInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.Movie;
import com.fic.service.mapper.MovieMapper;
import com.fic.service.service.MovieService;
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
@Api("电影管理")
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
    @ApiOperation("新增")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "movieName", value = "电影名", required = true),
            @ApiImplicitParam(dataType = "BigDecimal", name = "budget", value = "总预算", required = true),
            @ApiImplicitParam(dataType = "BigDecimal", name = "quota", value = "开放额度", required = true),
            @ApiImplicitParam(dataType = "string", name = "showPlace", value = "上映地点", required = true),
            @ApiImplicitParam(dataType = "date", name = "showTime", value = "上映时间", required = true),
            @ApiImplicitParam(dataType = "int", name = "status", value = "状态,(0，未上架)(1上架)", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestParam(value = "movieCoverFile",required = true) MultipartFile movieCoverFile,
                              @RequestParam(value = "movieName",required = true) String movieName,
                              @RequestParam(value = "budget",required = true) BigDecimal budget,
                              @RequestParam(value = "quota",required = true) BigDecimal quota,
                              @RequestParam(value = "showPlace",required = true) String showPlace,
                              @RequestParam(value = "showTime",required = true) Date showTime,
                              @RequestParam(value = "status",required = true) int status) {
        log.debug(" movie add Action !!!");
        Movie movie = new Movie();
        movie.setMovieName(movieName);
        movie.setBudget(budget);
        movie.setQuota(quota);
        movie.setShowPlace(showPlace);
        movie.setShowTime(showTime);
        movie.setStatus((byte)status);
        ResponseVo responseVo = movieService.add(movie,movieCoverFile);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "电影ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "movieName", value = "电影名", required = true),
//            @ApiImplicitParam(dataType = "BigDecimal", name = "budget", value = "总预算", required = true),
//            @ApiImplicitParam(dataType = "BigDecimal", name = "quota", value = "开放额度", required = true),
//            @ApiImplicitParam(dataType = "string", name = "showPlace", value = "上映地点", required = true),
//            @ApiImplicitParam(dataTypeClass = Date.class,name = "showTime", value = "上映时间", required = true),
            @ApiImplicitParam(dataType = "int", name = "status", value = "状态,(0，未上架)(1上架)", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(@RequestParam(value = "movieCoverFile",required = false) MultipartFile movieCoverFile,
                                 @RequestParam(value = "id") int id,
                                 @RequestParam(value = "movieName") String movieName,
//                                 @RequestParam(value = "budget") BigDecimal budget,
//                                 @RequestParam(value = "quota") BigDecimal quota,
//                                 @RequestParam(value = "showPlace") String showPlace,
//                                 @RequestParam(value = "showTime") Date showTime,
                                 @RequestParam(value = "status") int status) {
        log.debug(" movie update Action !!!");
        Movie movie = new Movie();
        movie.setMovieName(movieName);
//        movie.setBudget(budget);
//        movie.setQuota(quota);
//        movie.setShowPlace(showPlace);
//        movie.setShowTime(showTime);
        movie.setStatus((byte)status);
        movie.setMovieId(id);
        ResponseVo responseVo = movieService.update(movie,movieCoverFile);
        return ResponseEntity.ok(responseVo);
    }

    @GetMapping("/onShelf")
    @ApiOperation("上架")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "movie ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity onShelf(@RequestParam Integer id) {
        log.debug(" movie onShelf Action !!!");
        ResponseVo responseVo = movieService.onShelf(id);
        return ResponseEntity.ok(responseVo);
    }


    @GetMapping("/shelf")
    @ApiOperation("下架")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "movie ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity shelf(@RequestParam Integer id) {
        log.debug(" movie shelf Action !!!");
        ResponseVo responseVo = movieService.shelf(id);
        return ResponseEntity.ok(responseVo);
    }
}
