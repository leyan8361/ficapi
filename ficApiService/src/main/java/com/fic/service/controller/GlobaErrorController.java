package com.fic.service.controller;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *   @Author Xie
 *   @Date 2018/11/22
 *   @Discription:
**/
@RestController
@Api(hidden = true)
public class GlobaErrorController implements ErrorController {

    private final Logger log = LoggerFactory.getLogger(GlobaErrorController.class);

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public ResponseEntity handleError(HttpServletRequest request, HttpServletResponse response){
        Integer statusCode  = (Integer) request.getAttribute("javax.servlet.error.status_code");
        log.debug("ErrorCode : " + statusCode);
        if(null == statusCode){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorCodeEnum.SYSTEM_EXCEPTION.getMsg());
        }
        ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.matchCode(statusCode);
        if(null != errorCodeEnum){
            ResponseVo responseVo = new ResponseVo(errorCodeEnum,null);
            return ResponseEntity.status(HttpStatus.OK).body(responseVo);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}