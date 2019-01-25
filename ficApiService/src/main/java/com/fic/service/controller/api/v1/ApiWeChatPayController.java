package com.fic.service.controller.api.v1;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.utils.DateUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2019/1/25
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1/weChat")
@Api(description = "Api-微信支付")
public class ApiWeChatPayController {

    private final Logger log = LoggerFactory.getLogger(ApiWeChatPayController.class);

    @GetMapping("/preOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "imei", value = "手机IMEI号", required = true),
            @ApiImplicitParam(dataType = "double", name = "amount", value = "金额", required = true),
            @ApiImplicitParam(dataType = "string", name = "openId", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    @ApiOperation("预下订单")
    public ResponseEntity bet(@RequestParam("userId") int userId,
                              @RequestParam("imei") String imei,
                              @RequestParam("amount") BigDecimal amount,
                              @RequestParam("openId") String openId
    ) {
        log.debug(" we chat pre order!!!");

        return ResponseEntity.ok().build();
    }
}
