package com.fic.service.service;

import com.fic.service.Enum.ErrorCodeEnum;

/**
 *   @Author Xie
 *   @Date 2018/11/28
 *   @Discription:
**/
public interface SmsService {

        ErrorCodeEnum send(String telphone);

        ErrorCodeEnum check(String telephone, String code);
}
