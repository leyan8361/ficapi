package com.fic.service.controller;

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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@RestController
@RequestMapping("/backend/banner")
@Api(description = "广告轮播图相关")
public class BannerController {

    private final Logger log = LoggerFactory.getLogger(BannerController.class);

    @Autowired
    BannerInfoService bannerInfoService;

    @GetMapping("/getAll")
    @ApiOperation("Api-获取所有")
    @ApiResponses({
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = BannerInfoVo.class)
    })
    public ResponseEntity getAll() {
        log.debug("banner getAll Action !!!");
        List<BannerInfoVo> resultList = bannerInfoService.getAll();
        if(resultList.isEmpty()){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
    }

    @PostMapping(value = "/add")
    @ApiOperation("Api-新增")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "inOrder", value = "顺序,int", required = true),
            @ApiImplicitParam(dataType = "string", name = "jumpUrlAndroid", value = "安卓跳转URL"),
            @ApiImplicitParam(dataType = "string", name = "jumpUrlIos", value = "ios跳转URL"),
            @ApiImplicitParam(dataType = "int", name = "status", value = "状态,(0，未上架)(1上架)", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestParam(value = "bannerFile") MultipartFile bannerFile,
                              @RequestParam(value = "inOrder") Integer order,
                              @RequestParam(value = "jumpUrlAndroid",required = false) String jumpUrlAndroid,
                              @RequestParam(value = "jumpUrlIos",required = false) String jumpUrlIos,
                              @RequestParam(value = "status") Byte status) {
        log.debug(" Api banner add Action !!!");
        BannerInfoVo bannerInfoVo = new BannerInfoVo();
        bannerInfoVo.setInOrder(order);
        bannerInfoVo.setJumpUrlAndroid(jumpUrlAndroid);
        bannerInfoVo.setJumpUrlIos(jumpUrlIos);
        bannerInfoVo.setStatus(status);
        ResponseVo responseVo = bannerInfoService.add(bannerInfoVo,bannerFile);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping("/update")
    @ApiOperation("Api-修改")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "BannerID", required = true),
            @ApiImplicitParam(dataType = "int", name = "inOrder", value = "顺序,int", required = true),
            @ApiImplicitParam(dataType = "string", name = "jumpUrlAndroid", value = "安卓跳转URL"),
            @ApiImplicitParam(dataType = "string", name = "jumpUrlIos", value = "ios跳转URL"),
            @ApiImplicitParam(dataType = "int", name = "status", value = "状态,(0，未上架)(1上架)", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(@RequestParam(value = "bannerFile",required = false) MultipartFile bannerFile,
                                 @RequestParam(value = "id") Integer id,
                                 @RequestParam(value = "inOrder") Integer order,
                                 @RequestParam(value = "jumpUrlAndroid",required = false) String jumpUrlAndroid,
                                 @RequestParam(value = "jumpUrlIos",required = false) String jumpUrlIos,
                                 @RequestParam(value = "status") Byte status) {
        log.debug(" Api banner update Action !!!");
        BannerInfoVo bannerInfoVo = new BannerInfoVo();
        bannerInfoVo.setInOrder(order);
        bannerInfoVo.setJumpUrlAndroid(jumpUrlAndroid);
        bannerInfoVo.setJumpUrlIos(jumpUrlIos);
        bannerInfoVo.setStatus(status);
        bannerInfoVo.setId(id);
        ResponseVo responseVo = bannerInfoService.update(bannerInfoVo,bannerFile);
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
        log.debug(" Api banner delete Action !!!");
        ResponseVo responseVo = bannerInfoService.delete(id);
        return ResponseEntity.ok(responseVo);
    }
}
