package com.fic.service.controller.api;

import com.fic.service.controller.HomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Xie
 * @Date 2018/11/23
 * @Discription:Api首页
 **/
@RestController
@RequestMapping("/api/v1")
@Api(description = "Api-首页")
public class ApiHomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    //TODO
    @GetMapping("/home")
    @ApiOperation("Api-获取首页数据")
    public ResponseEntity home() {
        log.debug(" Home Page !!!");
        return ResponseEntity.ok().body("success");
    }

}
