package com.fic.service.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *   @Author Xie
 *   @Date 2018/12/13
 *   @Discription:
**/
@Controller
public class SwaggerController {

    private final Logger log = LoggerFactory.getLogger(SwaggerController.class);

    @RequestMapping("/swagger-ui.html?{urlParam}")
    public void authSwagger(HttpServletRequest servletRequest, HttpServletResponse response, @PathVariable String urlParam) {

        log.debug("loginSuccess Action!!");


    }
}
