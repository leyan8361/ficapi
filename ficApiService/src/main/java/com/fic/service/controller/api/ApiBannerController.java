package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.BannerInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.service.BannerInfoService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/14
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1/banner")
@Api(description = "Api-广告图，轮播图相关")
public class ApiBannerController {

    private final Logger log = LoggerFactory.getLogger(ApiBannerController.class);

    @Autowired
    BannerInfoService bannerInfoService;

    @GetMapping("/getAll")
    @ApiOperation("Api-获取所有")
    @ApiResponses({
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = BannerInfoVo.class)
    })
    public ResponseEntity getAll() {
        log.debug(" Api banner getAll Action !!!");
        List<BannerInfoVo> resultList = bannerInfoService.getAll();
        if(resultList.isEmpty()){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
    }


}
