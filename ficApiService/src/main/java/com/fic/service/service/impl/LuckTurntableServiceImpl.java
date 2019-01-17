package com.fic.service.service.impl;

import com.fic.service.Enum.*;
import com.fic.service.Vo.*;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.LuckTurntableService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LuckTurntableServiceImpl implements LuckTurntableService {

    private final Logger log = LoggerFactory.getLogger(LuckTurntableServiceImpl.class);

    @Autowired
    LuckyTurntableMapper luckyTurntableMapper;
    @Autowired
    LuckyRecordMapper luckyRecordMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UploadProperties uploadProperties;
    @Autowired
    InvestMapper investMapper;
    @Autowired
    BalanceStatementMapper balanceStatementMapper;
    @Autowired
    FileUtil fileUtil;

    @Override
    public ResponseVo getAll() {
        List<LuckyTurntable> resultList = luckyTurntableMapper.findAll();
        for(LuckyTurntable luckyTurntable: resultList){
            luckyTurntable.setPriceUrl(uploadProperties.getUrl(luckyTurntable.getPriceUrl()));
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo uploadCoverFile(MultipartFile coverFile) {
        String fileType = "";
        String fileName = coverFile.getOriginalFilename();
        if(!RegexUtil.isPic(fileName)){
            return new ResponseVo(ErrorCodeEnum.PIC_ERROR,null);
        }
        fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String newName = DateUtil.getTimeStamp()+"."+fileType;
        String newDir = Constants.LUCK_COVER_PATH+"/";
        String newPath = newDir+newName;
        ErrorCodeEnum saveCode = fileUtil.saveFile(coverFile,newDir,newName);
        if(!saveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(saveCode,null);
        int updateResult = luckyTurntableMapper.updateCover(newPath);
        if(updateResult <=0){
            log.error("上传转盘封面失败，奖品无数据");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(LuckTurntableAddVo luckTurntableAddVo) {
        LuckyTurntable luckyTurntable = new LuckyTurntable();
        luckyTurntable.setPriceName(luckTurntableAddVo.getPriceName());
        luckyTurntable.setPriceType(luckTurntableAddVo.getPriceType());
        luckyTurntable.setProbability(luckTurntableAddVo.getProbability());
        luckyTurntable.setAmount(luckTurntableAddVo.getAmount());
        luckyTurntable.setCreatedTime(new Date());
        int saveResult = luckyTurntableMapper.insertSelective(luckyTurntable);
        if(saveResult <=0){
            log.error(" 保存奖品失败 ");
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo update(LuckTurntableUpdateVo luckTurntableUpdateVo) {
        LuckyTurntable luckyTurntable = luckyTurntableMapper.get(luckTurntableUpdateVo.getId());
        if(null == luckyTurntable){
            log.error("更新奖品失败，查无此奖品 id :{}",luckTurntableUpdateVo.getId());
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        if(StringUtils.isNotEmpty(luckTurntableUpdateVo.getPriceName())){
            luckyTurntable.setPriceName(luckTurntableUpdateVo.getPriceName());
        }
        if(null != luckTurntableUpdateVo.getPriceType()){
            luckyTurntable.setPriceType(luckTurntableUpdateVo.getPriceType());
        }
        if(null != luckTurntableUpdateVo.getProbability() && luckTurntableUpdateVo.getProbability().compareTo(BigDecimal.ZERO) > 0){
            luckyTurntable.setProbability(luckTurntableUpdateVo.getProbability());
        }
        if(null !=luckTurntableUpdateVo.getAmount() && luckTurntableUpdateVo.getAmount().compareTo(BigDecimal.ZERO) >0){
            luckyTurntable.setAmount(luckTurntableUpdateVo.getAmount());
        }
        int saveResult = luckyTurntableMapper.updateByPrimaryKeySelective(luckyTurntable);
        if(saveResult <=0){
            log.error(" 更新奖品失败 ");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo delete(int id) {
        int deleteResult = luckyTurntableMapper.deleteByPrimaryKey(id);
        if(deleteResult <=0){
            log.error("删除奖品失败，无此奖品 id :{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo getPrice() {
        List<LuckyTurntable> findResult = luckyTurntableMapper.findAll();
        LuckTurntableInfoVo result = new LuckTurntableInfoVo();
        List<LuckTurntablePriceVo> resultList = new ArrayList<>();
        List<LuckTurntableBroadcastVo> broadcasts = new ArrayList<>();
        String luckCover = "";
        for(LuckyTurntable luckyTurntable : findResult){
            LuckTurntablePriceVo price = new LuckTurntablePriceVo();
            price.setPriceId(luckyTurntable.getId());
            price.setPriceName(luckyTurntable.getPriceName());
            price.setProbability(luckyTurntable.getProbability());
            if(StringUtils.isEmpty(luckCover)){
                luckCover = uploadProperties.getUrl(luckyTurntable.getPriceUrl());
            }
            resultList.add(price);
        }

        List<LuckyRecord> luckyRecords = luckyRecordMapper.findLastTen();
        for(LuckyRecord record : luckyRecords){
            LuckTurntableBroadcastVo broadcastVo = new LuckTurntableBroadcastVo();
            for(LuckyTurntable luckyTurntable : findResult){
                if(luckyTurntable.getId().equals(record.getBingoPrice())){
                    broadcastVo.setPriceName(luckyTurntable.getPriceName());
                    break;
                }
            }
            User user = userMapper.get(record.getUserId());
            if(null == user){
                log.error("getPrice 查找用户查询 id :{}",record.getUserId());
                throw new RuntimeException();
            }
            broadcastVo.setTelephone(RegexUtil.replaceTelephone(user.getUserName()));
            broadcasts.add(broadcastVo);
        }

        result.setCoverUrl(luckCover);
        result.setPriceList(resultList);
        result.setBroadcastList(broadcasts);
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    public ResponseVo getBingoRecord(int userId,int pageNum) {
        PageVo pageVo = new PageVo();
        pageVo.setPageNum(pageNum);
        User user = userMapper.get(userId);
        if(null == user){
            log.error("获取中奖记录失败，用户不存在id :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        List<LuckTurntableRecordVo> resultList = new ArrayList<>();
        List<LuckyRecord> findByUserId = luckyRecordMapper.findByUserIdWithPage(userId,pageVo.getPageNum());
        for(LuckyRecord findResult: findByUserId){
            LuckTurntableRecordVo result = new LuckTurntableRecordVo();
            if(null != findResult.getBingoPrice()){
                LuckyTurntable luckyTurntable = luckyTurntableMapper.get(findResult.getBingoPrice());
                if(null == luckyTurntable){
                    log.error("获取记录异常，奖励已不存在");
                }
                result.setPriceName(luckyTurntable.getPriceName());
            }

            result.setRecordId(findResult.getId());
            result.setBingoTime(findResult.getCreatedTime());
            result.setIsReceive(findResult.getStatus());
            resultList.add(result);
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo draw(Integer userId, Integer priceId, Integer status) {
        BigDecimal fee = new BigDecimal("20");
        LuckyRecord luckyRecord = new LuckyRecord();
        User user = userMapper.get(userId);
        if(null == user){
            log.error("查询用户不存在 user id :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        if(null == status){
            log.error("状态参数丢失，抽奖");
        }else{
            LuckyTurntable luckyTurntable = luckyTurntableMapper.get(priceId);
            if(null == luckyTurntable){
                log.error("查询奖品失败，turntable id :{}",priceId);
                return new ResponseVo(ErrorCodeEnum.LUCK_TURNTABLE_NOT_EXIST,null);
            }
            if(status == DrawStatusEnum.BINGO.code()){
                /** 中奖 */
                luckyRecord.setBingoPrice(priceId);
            }
        }
        luckyRecord.setStatus(status);
        luckyRecord.setCreatedTime(new Date());
        luckyRecord.setIsReceive(BooleanStatusEnum.NO.code());
        luckyRecord.setUserId(userId);
        int saveResult = luckyRecordMapper.insertSelective(luckyRecord);
        if(saveResult <=0){
            log.error("保存抽奖数据失败 luck record : {}",luckyRecord.toString());
            throw new RuntimeException();
        }

        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(userId);
        balanceStatement.setAmount(fee);
        balanceStatement.setWay(FinanceWayEnum.OUT.getCode());
        balanceStatement.setType(FinanceTypeEnum.DRAW.getCode());
        balanceStatement.setCreatedTime(new Date());

        Invest invest = investMapper.findByUserId(userId);
        if(null == invest){
            log.error("抽奖，查询用户资产失败 user id :{}",userId);
            throw new RuntimeException();
        }

        int saveBalanceResult = balanceStatementMapper.insertSelective(balanceStatement);
        if(saveBalanceResult <=0){
            log.error("抽奖，保存余额变更记录失败");
            throw new RuntimeException();
        }

        BigDecimal balance = invest.getBalance();
        BigDecimal rewardBalance = invest.getRewardBalance();
        if((balance.add(rewardBalance)).compareTo(fee) <0){
            log.error("抽奖失败, 余额不足");
            return new ResponseVo(ErrorCodeEnum.INVEST_BALANCE_NOT_ENOUGH,null);
        }
        if(balance.compareTo(fee) <0){
            BigDecimal restBalance = rewardBalance.subtract(fee);
            int updateInvestResult = investMapper.updateRewardBalance(restBalance,userId);
            if(updateInvestResult <=0){
                log.error("更新奖励余额失败");
                throw new RuntimeException();
            }
        }else{
            BigDecimal restBalance = balance.subtract(fee);
            int updateInvestResult = investMapper.updateBalance(restBalance,userId);
            if(updateInvestResult <=0){
                log.error("更新余额失败");
                throw new RuntimeException();
            }
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo receive(Integer userId, Integer recordId) {
        User user = userMapper.get(userId);
        if(null == user){
            log.error("领取,查询用户不存在 user id :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        LuckyRecord record = luckyRecordMapper.selectByPrimaryKey(recordId);
        if(null == record){
            log.error("领取,查询抽奖记录失败，turntable id :{}",recordId);
            return new ResponseVo(ErrorCodeEnum.LUCK_RECORD_NOT_FOUND,null);
        }
        LuckyTurntable luckyTurntable = luckyTurntableMapper.get(record.getBingoPrice());
        if(null == luckyTurntable){
            log.error("查询奖品失败，turntable id :{}",record.getBingoPrice());
            return new ResponseVo(ErrorCodeEnum.LUCK_TURNTABLE_NOT_EXIST,null);
        }

        if(luckyTurntable.getPriceType() == PriceTypeEnum.COIN.code()){
            /** 币 */
            BalanceStatement balanceStatement = new BalanceStatement();
            balanceStatement.setType(FinanceTypeEnum.DRAW.getCode());
            balanceStatement.setAmount(luckyTurntable.getAmount());
            balanceStatement.setWay(FinanceWayEnum.IN.getCode());
            balanceStatement.setCreatedTime(new Date());
            balanceStatement.setUserId(userId);
            int saveBalance = balanceStatementMapper.insertSelective(balanceStatement);
            if(saveBalance <=0){
                log.error("领取，保存余额变更失败");
                throw new RuntimeException();
            }

            Invest invest = investMapper.findByUserId(userId);
            if(null == invest){
                log.error("领取，更新余额失败 user id :{}",userId);
                throw new RuntimeException();
            }
        }

        record.setIsReceive(BooleanStatusEnum.YES.code());

        int updateRecord = luckyRecordMapper.updateByPrimaryKey(record);
        if(updateRecord <=0){
            log.error("领取，更新抽奖记录失败");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
