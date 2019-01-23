package com.fic.service.controller;

import com.fic.service.Vo.LuckTurntableAddVo;
import com.fic.service.Vo.LuckTurntableUpdateVo;
import com.fic.service.Vo.OmLuckyRecordVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.UploadProperties;
import com.fic.service.service.LuckTurntableService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *   @Author Xie
 *   @Date 2019/1/17
 *   @Discription:
**/
@RestController
@RequestMapping("/backend/lucky")
@Api(description = "转盘奖品管理")
public class LuckyTurntableController {

    private final Logger log = LoggerFactory.getLogger(LuckyTurntableController.class);

    @Autowired
    LuckTurntableService luckTurntableService;
    @Autowired
    UploadProperties uploadProperties;

    @GetMapping("/getAll")
    @ApiOperation("查看所有")
    public ResponseEntity getAll() {
        log.debug(" lucky getAll!!!");
        ResponseVo result = luckTurntableService.getAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    @ApiOperation("增加")
    public ResponseEntity add(@RequestBody LuckTurntableAddVo luckTurntableAddVo) {
        log.debug(" lucky add!!!");
        ResponseVo result = luckTurntableService.add(luckTurntableAddVo);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update")
    @ApiOperation("更新")
    public ResponseEntity update(@RequestBody LuckTurntableUpdateVo luckTurntableUpdateVo) {
        log.debug(" lucky update!!!");
        ResponseVo result = luckTurntableService.update(luckTurntableUpdateVo);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除")
    public ResponseEntity delete(@RequestParam int id) {
        log.debug(" lucky delete!!!");
        ResponseVo result = luckTurntableService.delete(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/uploadCover")
    @ApiOperation("上传转盘图")
    public ResponseEntity uploadCover(@RequestBody MultipartFile coverFile) {
        log.debug(" lucky uploadCover!!!");
        ResponseVo result = luckTurntableService.uploadCoverFile(coverFile);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/onShelf")
    @ApiOperation("上架")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "礼品ID", required = true)
    })
    public ResponseEntity onShelf(@RequestParam int id) {
        log.debug(" lucky onShelf!!!");
        ResponseVo result = luckTurntableService.onShelf(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/offShelf")
    @ApiOperation("下架")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "礼品ID", required = true)
    })
    public ResponseEntity offShelf(@RequestParam int id) {
        log.debug(" lucky offShelf!!!");
        ResponseVo result = luckTurntableService.shelf(id);
        return ResponseEntity.ok(result);
    }

}
