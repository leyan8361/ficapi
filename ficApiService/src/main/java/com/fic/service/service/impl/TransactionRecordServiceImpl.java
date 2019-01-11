package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.TransactionStatusEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.TransactionRecord;
import com.fic.service.entity.User;
import com.fic.service.entity.Wallet;
import com.fic.service.mapper.TransactionRecordMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.mapper.WalletMapper;
import com.fic.service.service.TransactionRecordService;
import com.fic.service.utils.Web3jUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class TransactionRecordServiceImpl implements TransactionRecordService {

    private final Logger log = LoggerFactory.getLogger(TransactionRecordServiceImpl.class);

    @Autowired
    TransactionRecordMapper transactionRecordMapper;
    @Autowired
    Web3jUtil web3jUtil;
    @Autowired
    WalletMapper walletMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ServerProperties serverProperties;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo approve(int id,String remark) {

        TransactionRecord record = transactionRecordMapper.selectByPrimaryKey(id);
        if(null == record){
            log.error(" 数据异常 无此转账申请 id:{}",id);
            throw new RuntimeException();
        }
        //TODO
//        web3jUtil.


        int updateStatus = transactionRecordMapper.updateStatus(id, TransactionStatusEnum.WAIT_CONFIRM.getCode(),remark);
        if(updateStatus <=0){
            log.error(" 转账审批通过失败，id:{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo reject(int id, String remark) {
        int updateStatus = transactionRecordMapper.updateStatus(id, TransactionStatusEnum.REJECT.getCode(),remark);
        if(updateStatus <=0){
            log.error(" 转账审批拒绝失败，id:{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo doTransactionOut(int userId, BigDecimal amount,String toAddress) {
        User user = userMapper.get(userId);
        Wallet wallet = walletMapper.findByAddressByCompany(userId);
        if(null == wallet){
            log.error(" 无钱包 ");
        }
        web3jUtil.doTransactionOut(amount,user.getPayPassword(),serverProperties.getStoreLocation()+user.getId()+"/"+wallet.getKeystore(),toAddress);
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
