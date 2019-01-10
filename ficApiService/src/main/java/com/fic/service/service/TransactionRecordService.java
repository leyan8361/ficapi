package com.fic.service.service;

import com.fic.service.Vo.ResponseVo;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *   @Author Xie
 *   @Date 2019/1/9
 *   @Discription:
**/
public interface TransactionRecordService{

    ResponseVo approve(int id,String remark);

    ResponseVo reject(int id,String remark);

    ResponseVo doTransaction(int userId, BigInteger amount);
}
