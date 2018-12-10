package com.fic.service.service;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *   @Author Xie
 *   @Date 2018/12/10
 *   @Discription:
**/
public interface WalletService {

    String generateWalletAddress(Integer userId,String password);

    BigInteger queryBalance(String address);

    boolean rollInFic();

    boolean rollOutFic(Integer userId, BigDecimal amount, String toAddress);

}
