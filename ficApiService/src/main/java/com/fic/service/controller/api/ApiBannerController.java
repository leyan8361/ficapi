package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.BannerInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.service.BannerInfoService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/14
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1/banner")
@Api("Api-广告图，轮播图相关")
public class ApiBannerController {

    private final Logger log = LoggerFactory.getLogger(ApiBannerController.class);

    @Autowired
    BannerInfoService bannerInfoService;

    @GetMapping("/getAll")
    @ApiOperation("Api-获取所有")
    @ApiResponses({
            @ApiResponse(code = 4001, message = "BANNER_NOT_FOUND"),
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = BannerInfoVo.class)
    })
    public ResponseEntity getAll() {
        log.debug(" Api banner getAll Action !!!");
        List<BannerInfoVo> resultList = bannerInfoService.getAll();
        if(resultList.isEmpty()){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.BANNER_NOT_FOUND,null));
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
    }

    @PostMapping("/add")
    @ApiOperation("Api-新增")
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestBody BannerInfoVo bannerInfoVo) {
        log.debug(" Api banner add Action !!!");
        ResponseVo responseVo = bannerInfoService.add(bannerInfoVo);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping("/update")
    @ApiOperation("Api-修改")
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(@RequestBody BannerInfoVo bannerInfoVo) {
        log.debug(" Api banner update Action !!!");
        ResponseVo responseVo = bannerInfoService.update(bannerInfoVo);
        return ResponseEntity.ok(responseVo);
    }

    @GetMapping("/delete")
    @ApiOperation("Api-删除")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "Banner ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity delete(@RequestParam Integer id) {
        log.debug(" Api banner update Action !!!");
        ResponseVo responseVo = bannerInfoService.delete(id);
        return ResponseEntity.ok(responseVo);
    }
}
