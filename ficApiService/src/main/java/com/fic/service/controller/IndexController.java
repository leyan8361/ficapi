package com.fic.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@RestController
public class IndexController {

    @RequestMapping("/")
    public void index(){
        System.out.println("index !!!!!");
    }

    @RequestMapping("/csrf")
    public void csrf(){
        System.out.println("index !!!!!");
    }
}
