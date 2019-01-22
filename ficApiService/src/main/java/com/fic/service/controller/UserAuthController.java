package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.UserAuth;
import com.fic.service.service.UserAuthService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/userAuth")
@Api(description = "实名认证")
public class UserAuthController {

    private final Logger log = LoggerFactory.getLogger(UserAuthController.class);

    @Autowired
    UserAuthService userAuthService;

    @GetMapping("/getAllByType")
    @ApiOperation("Api-查询所有(0,查看所有)(1,查看待审核)(2,查看已审核的)")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "type", value = "查看类型", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = UserAuth.class)
    })
    public ResponseEntity getAllByType(@RequestParam int type) {
        log.debug(" do getAllByType action !!");
        List<UserAuth> result = userAuthService.findAllByStatus(type);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }

    @GetMapping("/auditing")
    @ApiOperation("Api-审核")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userAuthId", value = "实名认证ID", required = true),
            @ApiImplicitParam(dataType = "boolean", name = "pass", value = "(true,通过)(false,不通过)", required = true),
            @ApiImplicitParam(dataType = "string", name = "remark", value = "备注", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity auditing(@RequestParam int userAuthId,@RequestParam boolean pass,@RequestParam(required = false)String remark) {
        log.debug(" do auditing action !!");
        ResponseVo result = userAuthService.auditing(userAuthId,pass,remark);
        return ResponseEntity.ok(result);
    }
}
