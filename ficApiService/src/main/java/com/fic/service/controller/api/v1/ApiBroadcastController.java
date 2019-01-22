package com.fic.service.controller.api.v1;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.controller.BootPageController;
import com.fic.service.service.BroadcastService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/broadcast")
@Api(description = "Api-播报")
public class ApiBroadcastController {

    private final Logger log = LoggerFactory.getLogger(BootPageController.class);

    @Autowired
    BroadcastService broadcastService;

    @GetMapping("/getByType")
    @ApiOperation("根据类型获取播报信息最新10条")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "type", value = "播报类型(0,竞猜）(1,抽奖)"),
    })
    public ResponseEntity getByType(@RequestParam int type) {
        log.debug(" get broadcast !!!");
        ResponseVo result = broadcastService.getByType(type);
        return ResponseEntity.ok(result);
    }
}
