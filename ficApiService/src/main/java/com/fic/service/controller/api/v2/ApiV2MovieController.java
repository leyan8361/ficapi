package com.fic.service.controller.api.v2;

import com.fic.service.Vo.MovieDetailInfoVo;
import com.fic.service.Vo.MovieInfoVo;
import com.fic.service.Vo.MovieVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BalanceStatement;
import com.fic.service.mapper.BalanceStatementMapper;
import com.fic.service.service.MovieService;
import com.fic.service.utils.DateUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    BalanceStatementMapper balanceStatementMapper;

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

    @GetMapping("/test")
    @ApiOperation("Api-test")
    public ResponseEntity test() {
        List<BalanceStatement> result = balanceStatementMapper.findAll();
        Map<String,List<BalanceStatement>> sorted  = new HashMap<>();
        for(BalanceStatement balanceStatement: result){
            if(sorted.containsKey(balanceStatement.getUserId()+"")){
                sorted.get(balanceStatement.getUserId()+"").add(balanceStatement);
            }else{
                List<BalanceStatement> currentB = new ArrayList<BalanceStatement>();
                currentB.add(balanceStatement);
                sorted.put(balanceStatement.getUserId()+"",currentB);
            }
        }

        for(Map.Entry<String,List<BalanceStatement>> map: sorted.entrySet()) {
            List<BalanceStatement> needSorted = map.getValue();



            for(int i = 0 ; i < needSorted.size(); i++){

                for(int j = 0 ; j < needSorted.size(); j++){
                    if(!needSorted.get(i).getId().equals(needSorted.get(j).getId())){
                        BalanceStatement temp = null;
                        if(needSorted.get(i).getCreatedTime().compareTo(needSorted.get(j).getCreatedTime()) < 0){
//                            if(needSorted.get(i).getUserId() == 114){
//                                log.debug("logsfds");
//                            }
//                            System.out.println("排序前 : " + DateUtil.dateToStrMatSec(needSorted.get(i).getCreatedTime()) + " || " + DateUtil.dateToStrMatSec(needSorted.get(j).getCreatedTime()));
                            temp = needSorted.get(j);
                            needSorted.set(j,needSorted.get(i));
                            needSorted.set(i,temp);
//                            System.out.println("排序后 : " + DateUtil.dateToStrMatSec(needSorted.get(i).getCreatedTime()) + " || " + DateUtil.dateToStrMatSec(needSorted.get(j).getCreatedTime()));

                        }
                    }
                }
            }



                if(null == needSorted.get(0).getAmount() || needSorted.get(0).getAmount().compareTo(new BigDecimal("1000")) < 0){
                    System.out.println("id: " +needSorted.get(0).getId()+"用户ID :"+needSorted.get(0).getUserId()+" amount :" + needSorted.get(0).getAmount()
                            +" type : "+ needSorted.get(0).getType() +" created_time : "+ DateUtil.dateToStrMatSec(needSorted.get(0).getCreatedTime())
                    );
                }

        }
        return ResponseEntity.ok().build();
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
