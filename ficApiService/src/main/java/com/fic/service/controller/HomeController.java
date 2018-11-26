package com.fic.service.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription: Home Page
**/
@RestController
@RequestMapping("/backend")
@Api("首页")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    @ApiOperation("获取首页数据")
//    @RequiresAuthentication
    public ResponseEntity home() {
        log.debug(" Home Page !!!");
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/user")
    @ApiOperation("测试普通用户权限")
    public ResponseEntity user() {
        log.debug(" User Page !!!");
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/admin")
    @ApiOperation("测试管理员用户权限")
    public ResponseEntity admin() {
        log.debug(" Admin Page !!!");
        return ResponseEntity.ok().body("success");
    }
}
