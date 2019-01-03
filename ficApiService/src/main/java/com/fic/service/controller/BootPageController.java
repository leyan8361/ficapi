package com.fic.service.controller;


import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BootPage;
import com.fic.service.service.BootPageService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/backend/boot_page")
@Api(description = "引导页管理")
public class BootPageController {

    private final Logger log = LoggerFactory.getLogger(BootPageController.class);

    @Autowired
    BootPageService bootPageService;

    @GetMapping("/getAll")
    @ApiOperation("查看所有")
    public ResponseEntity getAll() {
        log.debug(" get All boot_page !!!");
        ResponseVo result = bootPageService.getAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/add")
    @ApiOperation("新增引导页")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "sort", value = "顺序，小的排前面", required = true),
            @ApiImplicitParam(dataType = "int", name = "isShow", value = "(0,不需要显示)(1,需要显示)", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(
                              @RequestParam(name = "sort",defaultValue = "1") Integer sort,
                              @RequestParam(name = "isShow",defaultValue = "0") Integer isShow,
                              @RequestParam(name = "pageCoverFile",defaultValue = "引导页图片") MultipartFile pageCoverFile
    ) {
        log.debug(" boot page add Action !!!");
        BootPage bootPage = new BootPage();
        bootPage.setSort(sort);
        bootPage.setIsShow(isShow);
        bootPage.setCreatedTime(new Date());
        ResponseVo result = bootPageService.add(bootPage,pageCoverFile);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,headers="content-type=multipart/form-data")
    @ApiOperation("修改引导页")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "sort", value = "顺序，小的排前面"),
            @ApiImplicitParam(dataType = "int", name = "isShow", value = "(0,不需要显示)(1,需要显示)"),
            @ApiImplicitParam(dataType = "int", name = "id", value = "引导页ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1018, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(
            @RequestParam(name = "sort",defaultValue = "1",required = false) int sort,
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "isShow",required = false) Integer isShow,
            @ApiParam(name = "pageCoverFile",value = "引导页图片") MultipartFile pageCoverFile
    ) {
        log.debug(" boot page update Action !!!");
        BootPage bootPage = new BootPage();
        bootPage.setSort(sort);
        bootPage.setId(id);
        bootPage.setIsShow(isShow);
        bootPage.setCreatedTime(new Date());
        ResponseVo result = bootPageService.update(bootPage,pageCoverFile);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/delete")
    @ApiOperation("删除引导页")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "引导页ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity delete(
            @RequestParam(name = "id") Integer id) {
        log.debug(" boot page delete Action !!!");
        ResponseVo result = bootPageService.delete(id);
        return ResponseEntity.ok(result);
    }
}
