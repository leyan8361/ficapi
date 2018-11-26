package com.fic.service.exception;

/**
 *   @Author Xie
 *   @Date 2018/11/23
 *   @Discription: 请求缺失token For Api
**/
public class TokenMissedException extends RuntimeException {

    public TokenMissedException() {
        super();
    }

    public TokenMissedException(String message) {
        super(message);
    }
}
