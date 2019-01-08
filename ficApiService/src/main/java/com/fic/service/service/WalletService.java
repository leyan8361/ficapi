package com.fic.service.service;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *   @Author Xie
 *   @Date 2018/12/10
 *   @Discription:
**/
public interface WalletService {

    /**
     * 创建用户钱包地址
     * @param userId
     * @param password
     * @return
     */
    String generateWalletAddress(Integer userId,String password);

    /***
     * 查询用户钱包余额
     * @param address
     * @return
     */
    BigInteger queryBalance(String address);

    /**
     * 转入
     * @return
     */
    boolean rollIn();

    /**
     * 转出
     * @param userId
     * @param amount
     * @param toAddress
     * @return
     */
    boolean rollOut(Integer userId, BigDecimal amount, String toAddress);

}
