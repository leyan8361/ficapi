package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.LoginUserInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.AccountService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *   @Author Xie
 *   @Date 2018/12/5
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1")
@Api("Api-资产变动记录相关")
public class ApiMoneyController {

    private final Logger log = LoggerFactory.getLogger(ApiMoneyController.class);

    @Autowired
    AccountService accountService;
    @Autowired
    UserMapper userMapper;


    @GetMapping("/tradeRecord")
    @ApiOperation("Api-交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Parameter Missed"),
            @ApiResponse(code = 1001, message = "User Not Exist"),
            @ApiResponse(code = 1000, message = "Password UnMatch"),
            @ApiResponse(code = 200, message = "SUCCESS",response = LoginUserInfoVo.class)
    })
    public ResponseEntity tradeRecord(@RequestParam Integer userId) {
        log.debug(" Api tradeRecord Action !!!");



        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,null));
    }
}
