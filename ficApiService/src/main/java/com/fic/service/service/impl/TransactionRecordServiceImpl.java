package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.FinanceTypeEnum;
import com.fic.service.Enum.FinanceWayEnum;
import com.fic.service.Enum.TransactionStatusEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.TransactionRecordService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.RegexUtil;
import com.fic.service.utils.Web3jUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

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
    @Autowired
    BalanceStatementMapper balanceStatementMapper;
    @Autowired
    InvestMapper investMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo approve(int id,String remark) {

        TransactionRecord record = transactionRecordMapper.selectByPrimaryKey(id);
        if(null == record){
            log.error(" 数据异常 无此转账申请 id:{}",id);
            throw new RuntimeException();
        }
        //TODO 判断是否有ETH
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

    /**
     *  测试 测试测试测试测试测试测试测试测试测试 转出
     * @param userId
     * @param amount
     * @param toAddress
     * @return
     */
    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo doTransactionOut(int userId, BigDecimal amount,String toAddress) {
        User user = userMapper.get(userId);
        Wallet wallet = walletMapper.findByAddressByCompany(userId);
        if(null == wallet){
            log.error(" 无钱包 user Id :{}",userId);
            return new ResponseVo(ErrorCodeEnum.WALLET_NOT_EXIST,null);
        }
        web3jUtil.doTransactionOut(amount,user.getPayPassword(),serverProperties.getStoreLocation()+user.getId()+"/"+wallet.getKeystore(),toAddress);

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    /**
     * 转入（人工）确认
     * @return
     */
    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo confirmTranIn(int userId, String fromAddress, String txHash,String coinType,BigDecimal amount,String remark,String inComeTime) {
        User user = userMapper.get(userId);
        if(null == user){
            log.error("转入确认异常，user not exist userId :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        Invest invest = investMapper.findByUserId(userId);
        if(null == invest){
            log.error("转入确认异常，invest not exist userId:{}",userId);
            return new ResponseVo(ErrorCodeEnum.INVEST_NOT_EXIST,null);
        }
        if(!RegexUtil.isCoinType(coinType)){
            log.error("币种类型错误,{}",coinType);
            return new ResponseVo(ErrorCodeEnum.COIN_TYPE_NOT_PERMIT,null);
        }
        TransactionRecord record = new TransactionRecord();
        record.setUserId(userId);
        record.setCoinType(coinType);
        record.setFromAddress(fromAddress);
        record.setTransactionHash(txHash);
        record.setAmount(amount);
        record.setWay(FinanceWayEnum.IN.getCode());
        record.setRemark(remark);
        record.setCreatedTime(DateUtil.toSecFormatDay(inComeTime));
        record.setStatus(TransactionStatusEnum.SUCCESS.getCode());

        /**
         * TODO 汇率
         */
        BigDecimal resultBalance = null;

        /**
         * 处理余额
         */
        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(userId);
        balanceStatement.setCreatedTime(DateUtil.toSecFormatDay(inComeTime));
        balanceStatement.setType(FinanceTypeEnum.RECHARGE.getCode());
        balanceStatement.setAmount(resultBalance);
        balanceStatement.setWay(FinanceWayEnum.IN.getCode());

        int saveBalanceResult = balanceStatementMapper.insertSelective(balanceStatement);
        if(saveBalanceResult <=0){
            log.error(" 确认转入，生成余额失败 balancestatement :{}",balanceStatement.toString());
            throw new RuntimeException();
        }

        BigDecimal totalBalance = invest.getBalance().add(resultBalance);

        int updateInvestResult = investMapper.updateBalance(totalBalance,userId);
        if(updateInvestResult <=0){
            log.error(" 确认转入，更新Invest失败，balance :{},userId:{}",totalBalance,userId);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
