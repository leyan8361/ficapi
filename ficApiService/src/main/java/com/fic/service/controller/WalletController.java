package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.User;
import com.fic.service.entity.Wallet;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.WalletService;
import io.swagger.annotations.*;
import okhttp3.Response;
import okhttp3.internal.http2.ErrorCode;
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
@RequestMapping("/backend/wallet")
@Api(description = "管理钱包相关")
public class WalletController {

    private final Logger log = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    WalletService walletService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ServerProperties serverProperties;

    @GetMapping("/getAllUserWallet")
    @ApiOperation("Api-查询所有用户钱包地址")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity getAllUserWallet() {
        log.debug(" getAllUserWallet Action !!!");
        List<Wallet> resultList = walletService.findAll();
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,resultList));
    }

    @GetMapping("/getWalletByUserId")
    @ApiOperation("Api-查询某个用户所有钱包地址")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity getWalletByUserId(@RequestParam int userId) {
        log.debug(" getWalletByUserId Action !!!");
        List<Wallet> result = walletService.findByUserId(userId);
        if(null == result){
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.WALLET_NOT_EXIST,null));
        }
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,result));
    }

    @GetMapping("/addWalletForAllUser")
    @ApiOperation("Api-给所有用户生成钱包地址")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity addWalletForAllUser() {
        log.debug(" addWalletForAllUser Action !!!");
        List<User> findAllNotGenerateWallet = userMapper.findMissingAddress();
        log.debug(" {}.user need to add new wallet ",findAllNotGenerateWallet.size());
        for(User user : findAllNotGenerateWallet){
            walletService.generateWalletAddress(user.getId());
        }
        return ResponseEntity.ok().build();
    }

}
