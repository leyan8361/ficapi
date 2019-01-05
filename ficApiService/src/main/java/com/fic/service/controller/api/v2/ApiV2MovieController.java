package com.fic.service.controller.api.v2;

import com.fic.service.Vo.MovieDetailInfoVo;
import com.fic.service.Vo.MovieInfoVo;
import com.fic.service.Vo.MovieVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.service.MovieService;
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
 *   @Author Xie
 *   @Date 2018/11/30
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v2")
@Api(description = "Api-Version_2 电影相关")
public class ApiV2MovieController {

    private final Logger log = LoggerFactory.getLogger(ApiV2MovieController.class);

    @Autowired
    MovieService movieService;

    @GetMapping("/getMovies")
    @ApiOperation("Api-获取电影列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "pageNum", value = "页码", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 4000, message = "MOVIE NOT FOUND"),
            @ApiResponse(code = 200, message = "SUCCESS",response = MovieVo.class)
    })
    public ResponseEntity getMovies(@RequestParam int pageNum) {
        log.debug(" Api get Movie List !!!");
        ResponseVo result = movieService.getMoviesV2(pageNum);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getMovieDetail")
    @ApiOperation("Api-获取电影详情")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "电影ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = MovieDetailInfoVo.class)
    })
    public ResponseEntity getMovieInfo(@RequestParam int userId, @RequestParam int movieId) {
        log.debug(" Api get Movie Details !!!");
        ResponseVo result = movieService.getMovieInfoV2(userId,movieId);
        return ResponseEntity.ok(result);
    }

}
