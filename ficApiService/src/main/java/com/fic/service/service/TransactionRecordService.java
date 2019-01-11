package com.fic.service.service;

import com.fic.service.Vo.ResponseVo;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2019/1/9
 *   @Discription:
**/
public interface TransactionRecordService{

    ResponseVo approve(int id,String remark);

    ResponseVo reject(int id,String remark);

    ResponseVo doTransactionOut(int userId, BigDecimal amount,String toAddress);
}
