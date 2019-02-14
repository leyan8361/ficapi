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
     * 删除所有用户钱包
     */
    ResponseVo deleteAll();
    /**
     * 删除某个用户钱包 OM删除
     */
    ResponseVo deleteByUserId(Integer userId);
    /**
     * 删除用户添加的钱包地址
     */
    ResponseVo deleteById(Integer id);


}
