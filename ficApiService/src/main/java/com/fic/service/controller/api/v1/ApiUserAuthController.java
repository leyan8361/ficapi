package com.fic.service.controller.api.v1;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.mapper.UserAuthMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.UserAuthService;
import com.fic.service.utils.FileUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *   @Author Xie
 *   @Date 2019/1/15
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1/userAuth")
@Api(description = "Api-实名认证")
public class ApiUserAuthController {

    private final Logger log = LoggerFactory.getLogger(ApiUserAuthController.class);

    @Autowired
    UserAuthMapper userAuthMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    UserAuthService userAuthService;


    @GetMapping("/get")
    @ApiOperation("Api-获取某个用户的实名认证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS"),
            @ApiResponse(code = 1028, message = "USER_NOT_AUTH")
    })
    public ResponseEntity get(@RequestParam int userId) {
        log.debug(" do get action !!");
        ResponseVo result = userAuthService.findByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    @ApiOperation("Api-上传身份证")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "cerId", value = "身份证ID", required = true),
            @ApiImplicitParam(dataType = "string", name = "name", value = "名字", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1024, message = "USER_AUTH_FRONT_FACE_MISSED"),
            @ApiResponse(code = 1025, message = "USER_AUTH_BACK_FACE_MISSED"),
            @ApiResponse(code = 1026, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 1027, message = "USER_ALREADY_AUTH"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity add(@RequestParam int userId, @RequestParam String cerId,@RequestParam String name,@ApiParam(required = true,value = "身份证正面") MultipartFile frontFaceFile,@ApiParam(required = true,value = "身份证反面") MultipartFile backFaceFile) {
        log.debug(" do add action !!");
        ResponseVo result = userAuthService.add(userId,cerId,name,frontFaceFile,backFaceFile);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update")
    @ApiOperation("Api-修改身份证")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "认证数据ID"),
            @ApiImplicitParam(dataType = "string", name = "cerId", value = "身份证ID"),
            @ApiImplicitParam(dataType = "string", name = "name", value = "名字")
    })
    @ApiResponses({
            @ApiResponse(code = 1024, message = "USER_AUTH_FRONT_FACE_MISSED"),
            @ApiResponse(code = 1025, message = "USER_AUTH_BACK_FACE_MISSED"),
            @ApiResponse(code = 1026, message = "ERROR PIC TYPE (png|jpg|bmp|jpeg)"),
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    public ResponseEntity update(@RequestParam int id, @RequestParam(required = false)  String cerId,@RequestParam(required = false)  String name,@ApiParam(required = false,value = "身份证正面") MultipartFile frontFaceFile,@ApiParam(required = false,value="身份证反面") MultipartFile backFaceFile) {
        log.debug(" do update action !!");
        ResponseVo result = userAuthService.update(id,cerId,name,frontFaceFile,backFaceFile);
        return ResponseEntity.ok(result);
    }

}
