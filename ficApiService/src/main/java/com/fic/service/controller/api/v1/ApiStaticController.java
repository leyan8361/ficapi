package com.fic.service.controller.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:
**/
@Controller
@RequestMapping("/api/v1/static")
@Api(description = "Api-条款")
public class ApiStaticController {

    private final Logger log = LoggerFactory.getLogger(ApiStaticController.class);

    @GetMapping(value = "/agreement")
    @ApiOperation("Api-淘影APP使用协议")
    public String agreement() {
        return "agreement";
    }

    @GetMapping(value = "/entrustment_agreement")
    @ApiOperation("Api-委托协议")
    public String entrustment() {
        return "entrustment_agreement";
    }

    @GetMapping(value = "/privacy_policy")
    @ApiOperation("Api-隐私条款")
    public String privacy() {
        return "privacy_policy";
    }

    @GetMapping(value = "/replacement_protocol")
    @ApiOperation("Api-置换协议")
    public String replacement() {
        return "replacement_protocol";
    }

    @GetMapping(value = "/bet_rules")
    @ApiOperation("Api-竞猜规则")
    public String betRules() {
        return "bet_rules";
    }

    @GetMapping(value = "/bet_agreement")
    @ApiOperation("Api-竞猜协议")
    public String betAgreement() {
        return "bet_agreement";
    }

}
