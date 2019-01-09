package com.fic.service.service;

import com.fic.service.Vo.DoTransactionVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.Wallet;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/10
 *   @Discription:
**/
public interface WalletService {

    /**
     * 创建用户钱包地址
     * @param userId
     * @return
     */
    Wallet generateWalletAddress(Integer userId);

    /**
     * 查询所有用户钱包地址
     */
    List<Wallet> findAll();

    /**
     * 查询某个用户所有钱包地址
     */
    List<Wallet> findByUserId(int userId);

    /***
     * 查询用户钱包余额
     * @return
     */
    ResponseVo queryBalanceByUserId(int userId);

    /**
     * 转入 转出
     * @return
     */
    ResponseVo doTransactionApply(DoTransactionVo transactionVo);

}
