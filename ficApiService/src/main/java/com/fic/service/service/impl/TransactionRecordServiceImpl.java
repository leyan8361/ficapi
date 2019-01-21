package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.FinanceTypeEnum;
import com.fic.service.Enum.FinanceWayEnum;
import com.fic.service.Enum.TransactionStatusEnum;
import com.fic.service.Vo.*;
import com.fic.service.constants.Constants;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.TransactionRecordService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.RegexUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        BigDecimal walletBalance = web3jUtil.getEthBalance(record.getFromAddress());
        if(BigDecimal.ZERO.compareTo(walletBalance) <= 0){
            log.debug("审批转出申请，地址ETH余额不足，无法转出 wallet adderss :{}",record.getFromAddress());
            return new ResponseVo(ErrorCodeEnum.TRAN_OUT_NOT_ENOUGH_GAS,"用户钱包地址无以太坊，无法转出");
        }
        Wallet wallet = walletMapper.findByAddressByCompany(record.getUserId());

        TransactionOutVo result = web3jUtil.doTransactionOut(record.getAmount(),wallet.getPassword(),wallet.getKeystore(),record.getToAddress());
        if(!result.isSuccess()){
            log.debug("审批转出申请，转出发送合约失败");
            return new ResponseVo(result.getErrorCodeEnum(),null);
        }
        record.setTransactionHash(result.getTxHash());
        record.setStatus(TransactionStatusEnum.WAIT_CONFIRM.getCode());
        record.setRemark(remark);
        int updateStatus = transactionRecordMapper.updateByPrimaryKey(record);
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
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo doTransactionApply(DoTransactionVo transactionVo) {
        User user = userMapper.get(transactionVo.getUserId());
        if(null == user){
            log.error("用户不存在");
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        Invest invest = investMapper.findByUserId(transactionVo.getUserId());
        if(null == invest){
            log.error("资产不存在");
            return new ResponseVo(ErrorCodeEnum.INVEST_NOT_EXIST,null);
        }
        if(invest.getBalance().compareTo(transactionVo.getAmount()) <0){
            log.debug("转出申请，余额不足 invest id :{},userId:{}",invest.getInvestId(),invest.getUserId());
            return new ResponseVo(ErrorCodeEnum.INVEST_BALANCE_NOT_ENOUGH,null);
        }

        Wallet wallet = walletMapper.findByAddressByCompany(transactionVo.getUserId());

        if(null == wallet){
            log.error("转出申请，钱包不存在");
            return new ResponseVo(ErrorCodeEnum.WALLET_NOT_EXIST,null);
        }

        BigDecimal walletBalance = web3jUtil.getEthBalance(wallet.getWalletAddress());
        if(BigDecimal.ZERO.compareTo(walletBalance) <= 0){
            log.debug("转出申请不允许，钱包余额不足， wallet adderss :{}",wallet.getWalletAddress());
            return new ResponseVo(ErrorCodeEnum.TRAN_OUT_NOT_ENOUGH_GAS,null);
        }

        TransactionRecord result = new TransactionRecord();
        result.setAmount(transactionVo.getAmount());
        result.setFromAddress(wallet.getWalletAddress());
        result.setToAddress(transactionVo.getToAddress());
        result.setStatus(TransactionStatusEnum.APPLY.getCode());
        result.setCreatedTime(new Date());
        result.setUserId(transactionVo.getUserId());
        result.setTransactionAddress(serverProperties.getContactAddress());
        result.setFee(BigDecimal.ZERO);
        result.setWay(FinanceWayEnum.OUT.getCode());
        result.setInComeTime(new Date());
        int saveResult = transactionRecordMapper.insertSelective(result);
        if(saveResult<=0){
            log.error(" 转账申请失败，");
            throw new RuntimeException();
        }

        BigDecimal balance = (null!=invest.getBalance()?invest.getBalance():BigDecimal.ZERO);
        BigDecimal resultBalance = balance.subtract(transactionVo.getAmount());
        BigDecimal lockBalance = (null!=invest.getLockBalance()?invest.getLockBalance():BigDecimal.ZERO);
        BigDecimal resultLockBalance = lockBalance.add(transactionVo.getAmount());
        int updateInvestResult = investMapper.updateLockBalance(resultBalance,resultLockBalance,transactionVo.getUserId());
        if(updateInvestResult <=0){
            log.error("转出申请，更新invest失败");
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
    @Deprecated
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
        record.setInComeTime(DateUtil.toSecFormatDay(inComeTime));
        record.setCreatedTime(new Date());
        record.setStatus(TransactionStatusEnum.SUCCESS.getCode());

        int saveRecordResult = transactionRecordMapper.insertSelective(record);
        if(saveRecordResult <=0){
            log.error("保存转账信息失败，record :{}",record.toString());
            throw new RuntimeException();
        }

        /**
         * 处理余额
         */
        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(userId);
        balanceStatement.setCreatedTime(DateUtil.toSecFormatDay(inComeTime));
        balanceStatement.setType(FinanceTypeEnum.RECHARGE.getCode());
        balanceStatement.setAmount(amount);
        balanceStatement.setWay(FinanceWayEnum.IN.getCode());
        balanceStatement.setCreatedTime(new Date());
        balanceStatement.setTraceId(record.getId());

        int saveBalanceResult = balanceStatementMapper.insertSelective(balanceStatement);
        if(saveBalanceResult <=0){
            log.error(" 确认转入，生成余额失败 balancestatement :{}",balanceStatement.toString());
            throw new RuntimeException();
        }

        BigDecimal totalBalance = invest.getBalance().add(amount);

        int updateInvestResult = investMapper.updateBalance(totalBalance,userId);
        if(updateInvestResult <=0){
            log.error(" 确认转入，更新Invest失败，balance :{},userId:{}",totalBalance,userId);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo approveForTFC(int id, String remark) {
        TransactionRecord record = transactionRecordMapper.selectByPrimaryKey(id);
        if(null == record) {
            log.error(" 数据异常 无此转账申请 id:{}", id);
            throw new RuntimeException();
        }
        if(StringUtils.isEmpty(record.getToAddress())){
            log.error("确认转账TFC，收款人不存在, tran record id :{},to address:{}",id,record.getToAddress());
            throw new RuntimeException();
        }
        User payee = userMapper.get(Integer.valueOf(record.getToAddress()));
        if(null == payee){
            log.error("确认转账TFC，收款人不存在, tran record id :{},to address:{}",id,record.getToAddress());
            throw new RuntimeException();
        }

        /** 生成收款人收账记录 */
        TransactionRecord payeeRecord = new TransactionRecord();
        payeeRecord.setUserId(payee.getId());
        payeeRecord.setInComeTime(new Date());
        payeeRecord.setStatus(TransactionStatusEnum.SUCCESS.getCode());
        payeeRecord.setWay(FinanceWayEnum.IN.getCode());
        payeeRecord.setFromAddress(record.getUserId().toString());
        payeeRecord.setAmount(record.getAmount());
        payeeRecord.setCreatedTime(new Date());
        payeeRecord.setCoinType(Constants.TFC);
        int savePayeeRecord = transactionRecordMapper.insertSelective(payeeRecord);
        if(savePayeeRecord <=0){
            log.error(" 确认转账, 生成收款人收账记录 tran id  :{}",record.getId());
            throw new RuntimeException();
        }

        /** 转账人 扣除转账金额 */
        BalanceStatement outbalance = new BalanceStatement();
        outbalance.setTraceId(record.getId());
        outbalance.setWay(FinanceWayEnum.OUT.getCode());
        outbalance.setAmount(record.getAmount());
        outbalance.setType(FinanceTypeEnum.TRANSFER_OUT.getCode());
        outbalance.setUserId(record.getUserId());
        outbalance.setCreatedTime(new Date());

        /** 收款人 增加转账金额 */
        BalanceStatement inBalance = new BalanceStatement();
        inBalance.setUserId(payee.getId());
        inBalance.setType(FinanceTypeEnum.PAYEE_IN.getCode());
        inBalance.setWay(FinanceWayEnum.IN.getCode());
        inBalance.setAmount(record.getAmount());
        inBalance.setTraceId(record.getId());
        inBalance.setCreatedTime(new Date());
        inBalance.setTraceId(payeeRecord.getId());
        Invest investPayer = investMapper.findByUserId(record.getUserId());
        if(null == investPayer){
            log.error(" 确认转账，无对应资产记录 user id :{}",record.getUserId());
            throw new RuntimeException();
        }
        Invest investPayee = investMapper.findByUserId(payee.getId());
        if(null == investPayee){
            log.error(" 确认转账，无对应收款人资产记录 user id :{}",payee.getId());
            throw new RuntimeException();
        }

        if(investPayer.getLockBalance().compareTo(record.getAmount()) >=0){
            BigDecimal resultLockBalance = investPayer.getLockBalance().subtract(record.getAmount());
            int updateLockBalance = investMapper.updateLockBalance(investPayer.getBalance(),resultLockBalance,investPayer.getUserId());
            if(updateLockBalance <=0){
                log.error(" 确认转账，更新付款人资产失败 user id :{}",record.getUserId());
                throw new RuntimeException();
            }
        }else{
            log.error(" 确认转账，付款人锁定余额，不足支付转账金额 ");
            throw new RuntimeException();
        }

        BigDecimal payeeBalance = investPayee.getBalance().add(record.getAmount());
        int updatePayeeInvest = investMapper.updateBalance(payeeBalance,investPayee.getUserId());
        if(updatePayeeInvest <=0){
            log.error(" 确认转账， 更新收款人资产失败 user id :{}",investPayee.getUserId());
            throw new RuntimeException();
        }

        int savePayer = balanceStatementMapper.insertSelective(outbalance);
        if(savePayer <=0){
            log.error(" 确认转账， 生成付款人余额变更记录失败");
            throw new RuntimeException();
        }
        int savePayee = balanceStatementMapper.insertSelective(inBalance);
        if(savePayee <=0){
            log.error(" 确认转账，生成收款人余额变更记录失败");
            throw new RuntimeException();
        }

        record.setStatus(TransactionStatusEnum.SUCCESS.getCode());
        record.setRemark(remark);
        int updateStatus = transactionRecordMapper.updateByPrimaryKey(record);
        if(updateStatus <=0){
            log.error(" 转账审批通过失败，id:{}",id);
            throw new RuntimeException();
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo rejectForTFC(int id, String remark) {

        TransactionRecord record = transactionRecordMapper.selectByPrimaryKey(id);
        if(null == record){
            log.error(" 拒绝转账，record id :{}",id);
            return new ResponseVo(ErrorCodeEnum.TRANSACTION_NOT_FOUND,null);
        }

        /** 释放付款方锁定金额 */
        Invest invest = investMapper.findByUserId(record.getUserId());
        if(null == invest){
            log.error(" 拒绝转账，付款方资产不存在 user id :{}",record.getUserId());
            throw new RuntimeException();
        }
        BigDecimal balance = invest.getBalance();
        BigDecimal lockBalance = invest.getLockBalance();
        if(lockBalance.compareTo(record.getAmount()) <0){
            log.error(" 拒绝转账，付款方锁定资产已不足以退回 user id:{}",record.getUserId());
            throw new RuntimeException();
        }
        balance = balance.add(record.getAmount());
        lockBalance = lockBalance.subtract(record.getAmount());
        invest.setBalance(balance);
        invest.setLockBalance(lockBalance);
        int updateInvest = investMapper.updateByPrimaryKey(invest);
        if(updateInvest <=0){
            log.error(" 拒绝转账，更新付款方资产失败，user id :{}",record.getUserId());
            throw new RuntimeException();
        }
        int updateStatus = transactionRecordMapper.updateStatus(id, TransactionStatusEnum.REJECT.getCode(),remark);
        if(updateStatus <=0){
            log.error(" 转账审批拒绝失败，id:{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo doTransactionApply(DoTranTokenVo transactionVo) {
        User user = userMapper.get(transactionVo.getUserId());
        if(null == user){
            log.error("用户不存在");
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        if(user.getUserName().equals(transactionVo.getPayee())){
            return new ResponseVo(ErrorCodeEnum.TRAN_CAN_NOT_TO_SELF,null);
        }
        Invest invest = investMapper.findByUserId(transactionVo.getUserId());
        if(null == invest){
            log.error("资产不存在");
            return new ResponseVo(ErrorCodeEnum.INVEST_NOT_EXIST,null);
        }
        if(invest.getBalance().compareTo(transactionVo.getAmount()) <0){
            log.debug("转出申请，余额不足 invest id :{},userId:{}",invest.getInvestId(),invest.getUserId());
            return new ResponseVo(ErrorCodeEnum.INVEST_BALANCE_NOT_ENOUGH,null);
        }
        User payee = userMapper.findByUsername(transactionVo.getPayee());
        if(null == payee){
            log.error("收款人手机号不存在 username :{}",transactionVo.getPayee());
            return new ResponseVo(ErrorCodeEnum.PAYEE_NOT_EXIST,null);
        }
        TransactionRecord result = new TransactionRecord();
        result.setAmount(transactionVo.getAmount());
        result.setStatus(TransactionStatusEnum.APPLY.getCode());
        result.setCreatedTime(new Date());
        result.setUserId(transactionVo.getUserId());
        result.setToAddress(payee.getId()+"");
        result.setCoinType(Constants.TFC);
        result.setFee(BigDecimal.ZERO);
        result.setWay(FinanceWayEnum.OUT.getCode());
        result.setInComeTime(new Date());
        int saveResult = transactionRecordMapper.insertSelective(result);
        if(saveResult<=0){
            log.error(" 转账申请失败，");
            throw new RuntimeException();
        }
        BigDecimal balance = (null!=invest.getBalance()?invest.getBalance():BigDecimal.ZERO);
        BigDecimal resultBalance = balance.subtract(transactionVo.getAmount());
        BigDecimal lockBalance = (null!=invest.getLockBalance()?invest.getLockBalance():BigDecimal.ZERO);
        BigDecimal resultLockBalance = lockBalance.add(transactionVo.getAmount());
        int updateInvestResult = investMapper.updateLockBalance(resultBalance,resultLockBalance,transactionVo.getUserId());
        if(updateInvestResult <=0){
            log.error("转出申请，更新invest失败");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo getTransactionRecord(Integer userId, Integer pageNum) {
        List<AppTransactionRecordVo> resultList = new ArrayList<>();
        User user = userMapper.get(userId);
        if(null == user){
            log.error("查询转账记录失败，用户不存在");
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        PageVo page = new PageVo();
        page.setPageNum(pageNum);
        int offset = page.getPageNum()*10;
        List<TransactionRecord> findResult = transactionRecordMapper.findAllByUserIdAndPage(userId,offset);
        if(findResult.size() == 0){
            return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
        }
        for(TransactionRecord find: findResult){
            AppTransactionRecordVo result = new AppTransactionRecordVo();
            if(find.getWay() == FinanceWayEnum.OUT.getCode()){
                if(StringUtils.isEmpty(find.getToAddress())){
                    continue;
                }
                User payee = userMapper.get(Integer.valueOf(find.getToAddress()));
                if(null == payee){
                    log.debug("获取转账记录， 收款人查询失败 user id :{}",find.getToAddress());
                    continue;
                }
                result.setTelephone(RegexUtil.replaceTelephone(payee.getUserName()));
            }
            if(find.getWay() == FinanceWayEnum.IN.getCode()){
                if(StringUtils.isEmpty(find.getFromAddress())){
                    continue;
                }
                User payer = userMapper.get(Integer.valueOf(find.getFromAddress()));
                if(null == payer){
                    log.debug("获取转账记录，付款人查询失败 user id :{}",find.getFromAddress());
                    continue;
                }
                result.setTelephone(RegexUtil.replaceTelephone(payer.getUserName()));
            }
            result.setWay(find.getWay());
            result.setCreatedTime(find.getCreatedTime());
            result.setAmount(find.getAmount());
            result.setStatus(find.getStatus());
            result.setRemark(find.getRemark());
            resultList.add(result);
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }
}
