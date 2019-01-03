package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.MovieDetailInfoVo;
import com.fic.service.Vo.MovieInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.service.MovieService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/11/30
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1")
@Api(description = "Api-电影相关")
public class ApiMovieController {

    private final Logger log = LoggerFactory.getLogger(ApiMovieController.class);

    @Autowired
    MovieService movieService;

    @GetMapping("/getMovies")
    @ApiOperation("Api-获取电影列表")
    @ApiResponses({
            @ApiResponse(code = 4000, message = "MOVIE NOT FOUND"),
            @ApiResponse(code = 200, message = "SUCCESS",response = MovieInfoVo.class)
    })
    public ResponseEntity getMovies() {
        log.debug(" Api get Movie List !!!");
        List<MovieInfoVo> result = movieService.getAll();
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
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
        ResponseVo result = movieService.getMovieInfo(userId,movieId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/doLikeMovie")
    @ApiOperation("Api-点赞电影")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "电影ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity likeMovie(@RequestParam int userId, @RequestParam int movieId) {
        log.debug(" Api do like moive !!!");
        ResponseVo result = movieService.doLikeMovie(userId,movieId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/doFavMovie")
    @ApiOperation("Api-收藏电影")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "int", name = "movieId", value = "电影ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity favMovie(@RequestParam int userId, @RequestParam int movieId) {
        log.debug(" Api do Fav moive !!!");
        ResponseVo result = movieService.doFavMovie(userId,movieId);
        return ResponseEntity.ok(result);
    }

}
