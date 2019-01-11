package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.FinanceWayEnum;
import com.fic.service.Enum.TransactionStatusEnum;
import com.fic.service.Vo.DoTransactionVo;
import com.fic.service.Vo.GenerateWalletVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.TransactionRecord;
import com.fic.service.entity.User;
import com.fic.service.entity.Wallet;
import com.fic.service.mapper.TransactionRecordMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.mapper.WalletMapper;
import com.fic.service.service.WalletService;
import com.fic.service.utils.Web3jUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    ServerProperties serverProperties;
    @Autowired
    Web3jUtil web3jUtil;
    @Autowired
    UserMapper userMapper;
    @Autowired
    WalletMapper walletMapper;
    @Autowired
    TransactionRecordMapper transactionRecordMapper;

    @Override
    public List<Wallet> findAll() {
        List<Wallet> resultList = walletMapper.findAll();
        return resultList;
    }

    @Override
    public List<Wallet> findByUserId(int userId) {
        List<Wallet> result = walletMapper.findByUserId(userId);
        return result;
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public Wallet generateWalletAddress(Integer userId) {
        User user = userMapper.get(userId);
        if(null == user){
            log.error(" 用户不存在，创建钱包 user id :{}",userId);
            return null;
        }
        String path = serverProperties.getStoreLocation()+userId+"/";
        GenerateWalletVo result = web3jUtil.createAccount(user.getPassword(),path);
        Wallet wallet = new Wallet();
        wallet.setKeystore(result.getPath());
        wallet.setWalletAddress(result.getAddress());
        wallet.setUserId(userId);
        wallet.setCreatedTime(new Date());
        wallet.setUpdatedTime(new Date());
        wallet.setBalance(BigInteger.ZERO);
        wallet.setCoinType(Constants.TFC);
        wallet.setCreatedBy(1);//标记为公司生成的
        int saveResult = walletMapper.insertSelective(wallet);
        if(saveResult <=0){
            log.error(" 保存新建钱包失败 ");
            throw new RuntimeException();
        }
        log.debug(" 创建新钱包 用户ID :{},钱包地址:{},文件路径:{}",userId,result.getAddress(),result.getPath());
        return wallet;
    }

    @Override
    public ResponseVo queryBalanceByUserId(int userId) {
        Wallet wallet = walletMapper.findByAddressByCompany(userId);
        if(null == wallet){
            log.error(" 查询余额，查找钱包地址失败");
            return new ResponseVo(ErrorCodeEnum.WALLET_NOT_EXIST,null);
        }
        BigInteger balance = web3jUtil.getBalance(wallet.getWalletAddress());
        if(null == balance){
            log.error("查询钱包地址余额失败 : 用户钱包地址 :{}",wallet.getWalletAddress());
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,balance);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo doTransactionApply(DoTransactionVo transactionVo) {
        User user = userMapper.get(transactionVo.getUserId());
        if(null == user){
            log.error("用户不存在");
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        Wallet wallet = walletMapper.findByAddressByCompany(transactionVo.getUserId());
        TransactionRecord result = new TransactionRecord();
        result.setAmount(transactionVo.getAmount());
        result.setFromAddress(wallet.getWalletAddress());
        result.setToAddress(transactionVo.getToAddress());
        result.setStatus(TransactionStatusEnum.APPLY.getCode());
        result.setCreatedTime(new Date());
        result.setUserId(transactionVo.getUserId());
        result.setTransactionAddress(serverProperties.getContactAddress());
        result.setFee(BigDecimal.ONE);
        result.setWay(FinanceWayEnum.OUT.getCode());
        result.setGasLimit(BigDecimal.ONE);
        result.setGasPrice(BigDecimal.ONE);
        int saveResult = transactionRecordMapper.insertSelective(result);
        if(saveResult<=0){
            log.error(" 转账申请失败，");
            throw new RuntimeException();
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
