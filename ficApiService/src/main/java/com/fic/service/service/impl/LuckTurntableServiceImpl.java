package com.fic.service.service.impl;

import com.fic.service.Enum.*;
import com.fic.service.Vo.*;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.LuckTurntableService;
import com.fic.service.utils.*;
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
        LuckyTurntable exist = luckyTurntableMapper.selectCover();
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
        int saveResult = 0;
        if(null == exist){
            exist = new LuckyTurntable();
            exist.setPriceType(PriceTypeEnum.COVER.code());
            exist.setStatus(ShelfStatusEnum.ON_SHELF.getCode());
            exist.setCreatedTime(new Date());
            exist.setPriceUrl(newPath);
            saveResult = luckyTurntableMapper.insertSelective(exist);
        }else{
            exist.setPriceUrl(newPath);
            saveResult = luckyTurntableMapper.updateByPrimaryKey(exist);
        }
        if(saveResult <=0){
            log.error("上传转盘封面失败，奖品无数据");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }


    @Override
    public ResponseVo getLuckRecord(int condition) {
        List<LuckyRecord> findResult = luckyRecordMapper.findAllByReceive(condition);
        List<OmLuckyRecordVo> resultList = new ArrayList<>();
        for(LuckyRecord find: findResult){
            OmLuckyRecordVo result = new OmLuckyRecordVo();
            BeanUtil.copy(result,find);
            String telephone = userMapper.getUserNameByUserId(find.getUserId());
            if(StringUtils.isNotEmpty(telephone)){
                result.setTelephone(telephone);
            }
            resultList.add(result);
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo approveReceive(int recordId) {
        int updateResult = luckyRecordMapper.updateReceiveById(recordId);
        if(updateResult <=0){
            log.error("审核抽奖数据兑换失败");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }


    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo onShelf(int id) {
        int updateResult = luckyTurntableMapper.updateStatusById(id,ShelfStatusEnum.ON_SHELF.getCode());
        if(updateResult <=0){
            log.error("上架礼品失败");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo shelf(int id) {
        int updateResult = luckyTurntableMapper.updateStatusById(id,ShelfStatusEnum.SHELF.getCode());
        if(updateResult <=0){
            log.error("下架礼品失败");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(LuckTurntableAddVo luckTurntableAddVo) {
        LuckyTurntable luckyTurntable = new LuckyTurntable();
        if(luckTurntableAddVo.getPriceType() == PriceTypeEnum.WORD.code()){
            String words[] = luckTurntableAddVo.getPriceName().split(Constants.WORDS_CUT);
            if(words.length < 1){
                return new ResponseVo(ErrorCodeEnum.LUCK_WORD_MISSED,null);
            }
        }
        luckyTurntable.setPriceType(luckTurntableAddVo.getPriceType());
        luckyTurntable.setPriceName(luckTurntableAddVo.getPriceName());
        luckyTurntable.setProbability(luckTurntableAddVo.getProbability());
        luckyTurntable.setAmount(luckTurntableAddVo.getAmount());
        luckyTurntable.setCreatedTime(new Date());
        luckyTurntable.setStatus(ShelfStatusEnum.ON_SHELF.getCode());
        luckyTurntable.setSort(luckTurntableAddVo.getSort());
        int saveResult = luckyTurntableMapper.insertSelective(luckyTurntable);
        if(saveResult <=0){
            log.error(" 保存奖品失败 ");
            throw new RuntimeException();
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
            if(luckTurntableUpdateVo.getPriceType() == PriceTypeEnum.WORD.code()){
                String words[] = luckTurntableUpdateVo.getPriceName().split(Constants.WORDS_CUT);
                if(words.length < 1){
                    return new ResponseVo(ErrorCodeEnum.LUCK_WORD_MISSED,null);
                }
            }
        }
        if(null != luckTurntableUpdateVo.getProbability() && luckTurntableUpdateVo.getProbability().compareTo(BigDecimal.ZERO) > 0){
            luckyTurntable.setProbability(luckTurntableUpdateVo.getProbability());
        }
        if(null !=luckTurntableUpdateVo.getAmount() && luckTurntableUpdateVo.getAmount().compareTo(BigDecimal.ZERO) >0){
            luckyTurntable.setAmount(luckTurntableUpdateVo.getAmount());
        }
        if(null !=luckTurntableUpdateVo.getSort() && luckTurntableUpdateVo.getSort().compareTo(BigDecimal.ZERO) >0){
            luckyTurntable.setSort(luckTurntableUpdateVo.getSort());
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
        int checkIfBeingUsingByRecord = luckyRecordMapper.countByBingoPrice(id);
        if(checkIfBeingUsingByRecord >0){
            return new ResponseVo(ErrorCodeEnum.LUCK_TURNTABLE_IS_BEING_USING,null);
        }
        int deleteResult = luckyTurntableMapper.deleteByPrimaryKey(id);
        if(deleteResult <=0){
            log.error("删除奖品失败，无此奖品 id :{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo getPrice(int userId) {
        List<LuckyTurntable> findResult = luckyTurntableMapper.findAllOnShelf();
        LuckTurntableInfoVo result = new LuckTurntableInfoVo();
        List<LuckTurntablePriceVo> resultList = new ArrayList<>();
        for(LuckyTurntable luckyTurntable : findResult){
            if(luckyTurntable.getPriceType() == PriceTypeEnum.COVER.code()){
                result.setCoverUrl(uploadProperties.getUrl(luckyTurntable.getPriceUrl()));
                continue;
            }
            LuckTurntablePriceVo price = new LuckTurntablePriceVo();
            price.setPriceId(luckyTurntable.getId());
            if(luckyTurntable.getPriceType() == PriceTypeEnum.WORD.code()){
                String [] words = luckyTurntable.getPriceName().split(Constants.WORDS_CUT);
                if(words.length < 1){
                    log.error("获取转盘数据，金句miss");
                    throw new RuntimeException();
                }
                String word = words[RandomUtil.getRandomNum(words.length)];
                price.setPriceName(word);
            }else{
                price.setPriceName(luckyTurntable.getPriceName());
            }
            price.setPriceType(luckyTurntable.getPriceType());
            price.setProbability(luckyTurntable.getProbability());
            resultList.add(price);
        }
        Invest invest = investMapper.findByUserId(userId);
        if(null == invest){
            log.error("获取转盘信息失败，无用户资产 user id :{}",userId);
            throw new RuntimeException();
        }
        BigDecimal balance = invest.getBalance().add(invest.getRewardBalance());
        result.setBalance(balance);
        result.setPriceList(resultList);
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    public ResponseVo getBingoRecord(int userId,int pageNum,int type) {
        PageVo pageVo = new PageVo();
        pageVo.setPageNum(pageNum);
        User user = userMapper.get(userId);
        if(null == user){
            log.error("获取中奖记录失败，用户不存在id :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        List<LuckTurntableRecordVo> resultList = new ArrayList<>();
        List<LuckyRecord> findByUserId = luckyRecordMapper.findByUserIdWithPage(userId,pageVo.getPageNum()*10,type);
        for(LuckyRecord findResult: findByUserId){
            LuckTurntableRecordVo result = new LuckTurntableRecordVo();
            if(null != findResult.getBingoPrice()){
                LuckyTurntable luckyTurntable = luckyTurntableMapper.get(findResult.getBingoPrice());
                if(null == luckyTurntable){
                    log.error("获取记录异常，奖励已不存在");
                    throw new RuntimeException();
                }
                if(luckyTurntable.getPriceType() == PriceTypeEnum.WORD.code()){
                    result.setPriceName("影视金句");
                }else{
                    result.setPriceName(luckyTurntable.getPriceName());
                }
//                if(luckyTurntable.getPriceType() == PriceTypeEnum.WORD.code()){
//                    String words[] = luckyTurntable.getPriceName().split(Constants.WORDS_CUT);
//                    if(words.length < 1){
//                        continue;
//                    }
//                    String word = words[RandomUtil.getRandomNum(words.length)];
//                    result.setPriceName(word);
//                }
                result.setPriceType(luckyTurntable.getPriceType());
            }
            result.setRecordId(findResult.getId());
            result.setBingoTime(findResult.getCreatedTime());
            result.setIsReceive(findResult.getIsReceive());
            resultList.add(result);
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo draw(Integer userId, Integer priceId,String word) {
        BigDecimal fee = new BigDecimal("20");
        LuckyRecord luckyRecord = new LuckyRecord();
        User user = userMapper.get(userId);
        if(null == user){
            log.error("查询用户不存在 user id :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
        LuckyTurntable luckyTurntable = luckyTurntableMapper.get(priceId);
        if(null == luckyTurntable){
            log.error("查询奖品失败，turntable id :{}",priceId);
            return new ResponseVo(ErrorCodeEnum.LUCK_TURNTABLE_NOT_EXIST,null);
        }
        luckyRecord.setBingoPrice(priceId);
        luckyRecord.setStatus(BingoStatusEnum.BINGO.getCode());
        luckyRecord.setCreatedTime(new Date());
        if(luckyTurntable.getPriceType() == PriceTypeEnum.WORD.code()){
            if(StringUtils.isEmpty(word)){
                log.error("中奖金句，缺失金句");
                return new ResponseVo(ErrorCodeEnum.LUCK_WORD_MISSED,null);
            }
            String words[] = luckyTurntable.getPriceName().split(Constants.WORDS_CUT);
            for(int i = 0; i < words.length; i++){
                if(word.equals(words[i])){
                    luckyRecord.setTrace(i);
                    break;
                }
            }
            if(null == luckyRecord.getTrace()){
                log.error("金句异常,无匹配金句");
                throw new RuntimeException();
            }
        }
        if(luckyTurntable.getPriceType() == PriceTypeEnum.GIFT.code() ||
            luckyTurntable.getPriceType() == PriceTypeEnum.MOVIE_TICKET.code()
        ){
            luckyRecord.setIsReceive(BooleanStatusEnum.NO.code());
        }else{
            luckyRecord.setIsReceive(BooleanStatusEnum.YES.code());
        }
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

        if(balance.compareTo(fee) >= 0){
            balance = balance.subtract(fee);
        }else{
            if(rewardBalance.compareTo(fee)>=0){//余额不足，用奖励减
                BigDecimal restAmount = rewardBalance.subtract(fee);
                rewardBalance = restAmount;
            }else{//奖励不足，先减奖励
                BigDecimal restAmount = fee.subtract(rewardBalance);
                rewardBalance = BigDecimal.ZERO;
                balance = balance.subtract(restAmount);
            }
        }
        int updateInvestResult = investMapper.updateBalanceAndRewardBalance(balance,rewardBalance,userId);
        if(updateInvestResult <=0){
            log.error("更新奖励余额失败");
            throw new RuntimeException();
        }

        /** 币 */
        if(luckyTurntable.getPriceType() == PriceTypeEnum.TEN.code() ||
                luckyTurntable.getPriceType() == PriceTypeEnum.FIFTY.code() ||
                luckyTurntable.getPriceType() == PriceTypeEnum.FIVE_THOUSAND.code() ||
                luckyTurntable.getPriceType() == PriceTypeEnum.TWO_HUNDRED.code()
        ){
            BalanceStatement wonBalanceStatement = new BalanceStatement();
            wonBalanceStatement.setType(FinanceTypeEnum.DRAW.getCode());
            wonBalanceStatement.setAmount(luckyTurntable.getAmount());
            wonBalanceStatement.setWay(FinanceWayEnum.IN.getCode());
            wonBalanceStatement.setCreatedTime(new Date());
            wonBalanceStatement.setUserId(userId);
            int saveBalance = balanceStatementMapper.insertSelective(wonBalanceStatement);
            if(saveBalance <=0){
                log.error("领取，保存余额变更失败");
                throw new RuntimeException();
            }

            BigDecimal wonBalance = balance;
            wonBalance = wonBalance.add(wonBalanceStatement.getAmount());
            int updateInvest = investMapper.updateBalance(wonBalance,userId);
            if(updateInvest <=0){
                log.error("领取，更新资产失败，user id :{}",userId);
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
        if(null == record.getBingoPrice()){
            log.error("领取，该抽奖未中奖");
            return new ResponseVo(ErrorCodeEnum.LUCK_TURNTABLE_IS_UN_BINGO,null);
        }
        if(record.getIsReceive() == BooleanStatusEnum.YES.code()){
            return new ResponseVo(ErrorCodeEnum.LUCK_ALREADY_RECEIVE,null);
        }
        LuckyTurntable luckyTurntable = luckyTurntableMapper.get(record.getBingoPrice());
        if(null == luckyTurntable){
            log.error("查询奖品失败，turntable id :{}",record.getBingoPrice());
            return new ResponseVo(ErrorCodeEnum.LUCK_TURNTABLE_NOT_EXIST,null);
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
