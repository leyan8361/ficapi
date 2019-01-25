package com.fic.service.controller.api.v2;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.controller.HomeController;
import com.fic.service.entity.User;
import com.fic.service.mapper.DeviceMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.AccountService;
import com.fic.service.service.DistributionService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
@Api(description = "Api-账户相关")
public class ApiV2AccountController {

    private final Logger log = LoggerFactory.getLogger(ApiV2AccountController.class);

    @Autowired
    UserMapper userMapper;
    @Autowired
    AccountService accountService;
    @Autowired
    DistributionService distributionService;
    @Autowired
    DeviceMapper deviceMapper;

    @GetMapping(value = "/updatePayPassword")
    @ApiOperation("Api-修改支付密码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "oldPayPassword", value = "旧支付密码MD5结果", required = true),
            @ApiImplicitParam(dataType = "string", name = "newPayPassword", value = "新支付密码MD5结果", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity updatePayPassword(@RequestParam Integer userId, @RequestParam String oldPayPassword,@RequestParam String newPayPassword){
        log.debug(" do updatePayPassword Action !!!");
        ResponseVo result = accountService.updatePayPassword(userId,oldPayPassword,newPayPassword);
        return ResponseEntity.ok(result);
    }
}
