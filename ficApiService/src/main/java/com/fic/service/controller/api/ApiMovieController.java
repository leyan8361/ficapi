package com.fic.service.controller.api;

import com.fic.service.Vo.MovieInfoVo;
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
@RequestMapping("/api/v1")
@Api("Api-电影相关")
public class ApiMovieController {

    private final Logger log = LoggerFactory.getLogger(ApiMovieController.class);

    @Autowired
    MovieService movieService;

    @GetMapping("/getMovies")
    @ApiOperation("Api-获取电影列表")
    @ApiResponses({
            @ApiResponse(code = 404, message = "MOVIE NOT FOUND"),
            @ApiResponse(code = 200, message = "SUCCESS",response = MovieInfoVo.class)
    })
    public ResponseEntity getMovieInfo() {
        log.debug(" Api get Movie Info !!!");
        ResponseVo result = movieService.getMovieInfo();
        return ResponseEntity.ok(result);
    }

}
