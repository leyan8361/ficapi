package com.fic.service.controller;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.Movie;
import com.fic.service.service.AppVersionService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/backend/version")
@Api(description = "app版本管理")
public class AppVersionController {

    private final Logger log = LoggerFactory.getLogger(AppVersionController.class);

    @Autowired
    AppVersionService appVersionService;

    @GetMapping("/getAll")
    @ApiOperation("获取所有版本信息")
    @ApiResponses({
            @ApiResponse(code = 500, message = "System ERROR"),
            @ApiResponse(code = 200, message = "SUCCESS",response = Movie.class)
    })
    public ResponseEntity getAll() {
        log.debug(" app version  getAll Action !!!");
        ResponseVo result = appVersionService.getAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiOperation("新增版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "version", value = "版本号", required = true,example = "vX.X.X,vXX.XX.XX"),
            @ApiImplicitParam(dataType = "int", name = "deviceType", value = "客户端类型 [0,android],[1,ios]", required = true, example = "0")
    })
    @ApiResponses({
            @ApiResponse(code = 3201, message = "EXIST"),
            @ApiResponse(code = 3202, message = "NOT_EXIST"),
            @ApiResponse(code = 3203, message = "FILE TYPE NOT MATCH"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestParam(name = "version") String version,
                              @RequestParam(name = "deviceType") int deviceType,
                              @ApiParam(value = "对应版本的app安装文件",name = "appFile",required = false) MultipartFile appFile) {
        log.debug(" app version add Action !!!");
        ResponseVo responseVo = appVersionService.add(version,deviceType,appFile);
        return ResponseEntity.ok(responseVo);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "版本ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "version", value = "版本号", required = true,example = "vX.X.X,vXX.XX.XX"),
            @ApiImplicitParam(dataType = "int", name = "deviceType", value = "客户端类型 [0,android],[1,ios]", required = true, example = "0"),
            @ApiImplicitParam(dataType = "file", name = "appFile", value = "对应版本的app安装文件", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 3201, message = "EXIST"),
            @ApiResponse(code = 3202, message = "NOT_EXIST"),
            @ApiResponse(code = 3203, message = "FILE TYPE NOT MATCH"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(@RequestParam(value = "id") int id,
                                 @RequestParam(value = "version",required = false) String version,
                                 @RequestParam(value = "deviceType",required = false) Integer deviceType,
                                 @RequestParam(value = "appFile",required = false) MultipartFile appFile) {
        log.debug(" app version update Action !!!");
        ResponseVo responseVo = appVersionService.update(id,version,deviceType,appFile);
        return ResponseEntity.ok(responseVo);
    }
}
