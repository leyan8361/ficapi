package com.fic.service.service;

import com.fic.service.Vo.DoTranTokenVo;
import com.fic.service.Vo.DoTransactionVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.Vo.WalletAddAddressVo;

import java.math.BigDecimal;

/**
 *   @Author Xie
 *   @Date 2019/1/9
 *   @Discription:
**/
public interface TransactionRecordService{

    ResponseVo getAllTransaction(int type);

    //TODO
    ResponseVo approve(int id,String remark);
    //TODO
    ResponseVo reject(int id,String remark);

    ResponseVo addAddress(WalletAddAddressVo walletAddAddressVo);

    ResponseVo getAllAddress(Integer userId);

    ResponseVo approveForTFC(int id,String remark);

    ResponseVo rejectForTFC(int id,String remark);
    /**
     * 转出申请
     */
    //TODO
    ResponseVo doTransactionApply(DoTransactionVo transactionVo);

    /**
     * 转出申请For TFC
     */
    ResponseVo doTransactionApply(DoTranTokenVo transactionVo);

    /**
     * Test
     */
    @Deprecated
    ResponseVo doTransactionOut(int userId, BigDecimal amount,String toAddress);

    /**
     * 确认转入
     */
    ResponseVo confirmTranIn(int userId, String fromAddress, String txHash, String coinType, BigDecimal amount,String remark,String inComeTime);

    /**
     * 转账记录
     */
    ResponseVo getTransactionRecord(Integer userId,Integer pageNum);
}
