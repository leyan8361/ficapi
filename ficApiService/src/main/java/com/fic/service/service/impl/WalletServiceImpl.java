package com.fic.service.service.impl;

import com.fic.service.constants.ServerProperties;
import com.fic.service.service.WalletService;
import com.fic.service.utils.Web3jUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class WalletServiceImpl implements WalletService {

    private final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    ServerProperties serverProperties;
    @Autowired
    Web3jUtil web3jUtil;

    @Override
    public String generateWalletAddress(Integer userId,String password) {
        String path = serverProperties.getStoreLocation()+userId+"/";
        String address = web3jUtil.createAccount(password,path);
        if(StringUtils.isEmpty(address)){
            log.error("生成钱包地址失败 : 用户ID :{}",userId);
            throw new RuntimeException();
        }
        return address;
    }

    @Override
    public BigInteger queryBalance(String address) {
        BigInteger balance = web3jUtil.getBalance(address);
        if(null == balance){
            log.error("查询钱包地址余额失败 : 用户钱包地址 :{}",address);
            throw new RuntimeException();
        }
        return balance;
    }

    @Override
    public boolean rollIn() {
        //Web3j
        return false;
    }

    @Override
    public boolean rollOut(Integer userId, BigDecimal amount,String toAddress) {
        //Web3j
        return false;
    }
}
