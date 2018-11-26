//package com.fic.service.controller;
//
//import com.fic.service.Enum.ErrorCodeEnum;
//import com.fic.service.Vo.ResponseVo;
//import com.fic.service.exception.TokenMissedException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// *   @Author Xie
// *   @Date 2018/11/23
// *   @Discription:异常捕获
//**/
////@RestControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    @ExceptionHandler(TokenMissedException.class)
//    @ResponseBody
//    ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
//        HttpStatus status = getStatus(request);
//        log.debug(" Exception : " + ex);
//        if(ex instanceof TokenMissedException){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(ErrorCodeEnum.TOKEN_MISSED_HEADER,null));
//        }
//        return ResponseEntity.badRequest().body(ex);
//    }
//
//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
//
//}
