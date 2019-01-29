package com.fic.service.scheduled;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.FinanceTypeEnum;
import com.fic.service.Enum.FinanceWayEnum;
import com.fic.service.Enum.TransactionStatusEnum;
import com.fic.service.Vo.QueryTransactionResultVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.*;
import com.fic.service.mapper.BalanceStatementMapper;
import com.fic.service.mapper.InvestMapper;
import com.fic.service.mapper.TransactionRecordMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.utils.Web3jUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionScheduledService {

    private final Logger log = LoggerFactory.getLogger(TransactionScheduledService.class);

    @Autowired
    Web3jUtil web3jUtil;

    @Autowired
    TransactionRecordMapper transactionRecordMapper;

    @Autowired
    InvestMapper investMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BalanceStatementMapper balanceStatementMapper;
    /**
     * 查询交易
     * status (0,失败)(1,成功)(2,交易挂起)
     */
//    @Scheduled(cron = "*/30 * * * * ?")
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public void doQueryTransactionStatus() {
        log.debug(" do update transaction status !");
        List<TransactionRecord> waitConfirmTran = transactionRecordMapper.findAllWaitConfirm();
        for(TransactionRecord record : waitConfirmTran){
            String txHash = record.getTransactionHash();
            if(StringUtils.isEmpty(txHash)){
                log.debug("the transaction record has no txHash userId:{},tranId:{}",record.getUserId(),record.getId());
                continue;
            }
            User user = userMapper.get(record.getUserId());
            if(null == user){
                log.error("1.用户不存在， 查询交易 user id :{}",record.getUserId());
                continue;
            }

            Invest invest = investMapper.findByUserId(user.getId());
            if(null == invest){
                log.error("2.资产不存在，查询交易 user id :{}",record.getUserId());
                continue;
            }
            QueryTransactionResultVo result  = web3jUtil.queryTransactionStatus(txHash,record.getGasPrice());
            if(result.getStatus() == 0){
                /** 交易失败 */
                record.setStatus(TransactionStatusEnum.FAILED.getCode());
                /** 恢复 余额*/
                if(invest.getLockBalance().compareTo(record.getAmount()) >=0){
                    BigDecimal resultLockBalance = invest.getLockBalance().subtract(record.getAmount());
                    BigDecimal balance = invest.getBalance().add(record.getAmount());
                    int updateLockBalance = investMapper.updateLockBalance(balance,resultLockBalance,invest.getUserId());
                    if(updateLockBalance <=0){
                        log.error("3.更新资产失败，查询交易，user id :{}",record.getUserId());
                        throw new RuntimeException();
                    }
                }else{
                    log.error("4.监听到账，用户锁定金额不足以扣款，异常处理");
                    continue;
                }
                continue;
            }
            if(result.getStatus() != 1){
                continue;
            }
            record.setStatus(TransactionStatusEnum.SUCCESS.getCode());
            record.setFee(result.getGasUsed());
            record.setInComeTime(new Date());

            if(invest.getLockBalance().compareTo(record.getAmount()) >=0){
                BigDecimal resultLockBalance = invest.getLockBalance().subtract(record.getAmount());
                int updateLockBalance = investMapper.updateLockBalance(invest.getBalance(),resultLockBalance,invest.getUserId());
                if(updateLockBalance <=0){
                    log.error("3.更新资产失败，查询交易，user id :{}",record.getUserId());
                    throw new RuntimeException();
                }
            }else{
                log.error("4.监听到账，用户锁定金额不足以扣款，异常处理");
                continue;
            }

            /**
             * 处理余额
             */
//            BalanceStatement balanceStatement = new BalanceStatement();
//            balanceStatement.setUserId(user.getId());
//            balanceStatement.setCreatedTime(new Date());
//            balanceStatement.setType(FinanceTypeEnum.TRANSFER_OUT.getCode());
//            balanceStatement.setAmount(record.getAmount());
//            balanceStatement.setWay(FinanceWayEnum.OUT.getCode());
//            balanceStatement.setTraceId(record.getId());
//            int saveBalanceResult = balanceStatementMapper.insertSelective(balanceStatement);
//            if(saveBalanceResult <=0){
//                log.error("4.转出，生成余额失败 balance statement :{}",balanceStatement.toString());
//                throw new RuntimeException();
//            }
        }
        int updateResult = transactionRecordMapper.updateStatusForeachList(waitConfirmTran);
        if(waitConfirmTran.size() > 0 && updateResult <=0){
            log.error("update transaction status failed!");
            throw new RuntimeException();
        }
    }

//    @Scheduled(cron = "*/5 * * * * ?")
//    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
//    public void testQuery() {
//        log.debug(" do update transaction status !");
//        QueryTransactionResultVo result  = web3jUtil.queryTransactionStatus("0x3f40856eae2001d1995cb77074b8f8bc11562ae40b8243f9e66c0af83739cc5d",new BigDecimal(0.000000004));
//        log.debug(" txhash : {}  | from :{} | to :{} | gas Used :{}",result.getTransactionHash(),result.getFrom(),result.getTo(),result.getGasUsed());
//    }
}
