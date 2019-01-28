package com.fic.service.controller.api.v1;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.service.WeChatPayService;
import com.fic.service.utils.IpAddressUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/weChat/")
@Api(description = "Api-微信")
public class ApiWeChatNotifyController {

    private final Logger log = LoggerFactory.getLogger(ApiWeChatNotifyController.class);

    @Autowired
    WeChatPayService weChatPayService;

    @GetMapping("/notify")
    @ApiOperation("Api-异步回调")
    public void notify(HttpServletRequest request, HttpServletResponse response){
        log.debug(" do weChat notify action !!");
        try{
            InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "UTF-8");
            weChatPayService.notify(result);
        } catch (UnsupportedEncodingException e) {
            log.error("异步回调，异常 e:{}",e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("异步回调，异常 e:{}",e);
            e.printStackTrace();
        }
    }

    @PostMapping("/preOrder")
    @ApiOperation("Api-预下订单")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "amount", value = "金额", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS",response = BigInteger.class)
    })
    public ResponseEntity preOrder(@RequestParam Integer userId, @RequestParam String amount,HttpServletRequest request){
        log.debug(" do weChat preOrder action !!");
        ResponseVo result = weChatPayService.wxPay(amount,IpAddressUtil.getIPAddress(request),userId);
        return ResponseEntity.ok(result);
    }
}
