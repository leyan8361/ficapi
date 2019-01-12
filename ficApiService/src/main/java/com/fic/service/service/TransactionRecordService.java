package com.fic.service.service;

import com.fic.service.Vo.DoTransactionVo;
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

    /**
     * 转出申请
     */
    ResponseVo doTransactionApply(DoTransactionVo transactionVo);

    /**
     * Test
     */
    @Deprecated
    ResponseVo doTransactionOut(int userId, BigDecimal amount,String toAddress);

    /**
     * 确认转入
     */
    ResponseVo confirmTranIn(int userId, String fromAddress, String txHash, String coinType, BigDecimal amount,String remark,String inComeTime);
}
