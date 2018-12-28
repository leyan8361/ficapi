package com.fic.service.service.impl;

import com.fic.service.Enum.*;
import com.fic.service.Vo.*;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.BetScenceService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.RegexUtil;
import okhttp3.internal.http2.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Author Xie
 * @Date 2018/12/17
 * @Discription:
 **/
@Service
public class BetScenceServiceImpl implements BetScenceService {

    private final Logger log = LoggerFactory.getLogger(BetScenceServiceImpl.class);

    @Autowired
    BetScenceMapper betScenceMapper;
    @Autowired
    BetMovieMapper betMovieMapper;
    @Autowired
    BetScenceMovieMapper betScenceMovieMapper;
    @Autowired
    UploadProperties uploadProperties;
    @Autowired
    BoxOfficeMapper boxOfficeMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BetUserMapper betUserMapper;
    @Autowired
    InvestMapper investMapper;
    @Autowired
    BalanceStatementMapper balanceStatementMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(BetScenceVo betScenceVo) {
        if(null == betScenceVo){
            log.error(" add bet Scence失败  scence is null");
            return new ResponseVo(ErrorCodeEnum.BET_ADD_MOVIE_NOT_FOUND,null);
        }
        BetScence betScence = new BetScence();
        BeanUtil.copy(betScence,betScenceVo);
        if(null != betScence.getJasckpotFee() && betScence.getJasckpotFee().compareTo(BigDecimal.ZERO) >0){
            betScence.setJasckpotFee(betScence.getJasckpotFee().divide(new BigDecimal("100"),2));
        }
        if(null != betScence.getReservationFee() && betScence.getReservationFee().compareTo(BigDecimal.ZERO) > 0){
           betScence.setReservationFee(betScence.getReservationFee().divide(new BigDecimal("100"),2));
        }
        betScence.setCreatedTime(new Date());
        int addResult = betScenceMapper.insertSelective(betScence);
        if(addResult <=0){
            log.error(" add bet Scence 失败 ");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo update(BetScenceVo betScenceVo) {
        if(null == betScenceVo || null == betScenceVo.getId()){
            log.error(" update bet Scence失败  scence is null");
            return new ResponseVo(ErrorCodeEnum.BET_ADD_MOVIE_NOT_FOUND,null);
        }
        BetScence betScence = new BetScence();
        BeanUtil.copy(betScence,betScenceVo);
        if(null != betScence.getJasckpotFee() && betScence.getJasckpotFee().compareTo(BigDecimal.ZERO) >0){
            betScence.setJasckpotFee(betScence.getJasckpotFee().divide(new BigDecimal("100"),2));
        }
        if(null != betScence.getReservationFee() && betScence.getReservationFee().compareTo(BigDecimal.ZERO) > 0){
            betScence.setReservationFee(betScence.getReservationFee().divide(new BigDecimal("100"),2));
        }
        betScence.setCreatedTime(new Date());
        int addResult = betScenceMapper.updateByPrimaryKeySelective(betScence);
        if(addResult <=0){
            log.error(" update bet Scence 失败 ");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo onShelf(int id) {
        int updateResult = betScenceMapper.updateStatus(ShelfStatusEnum.ON_SHELF.getCode(),id);
        if(updateResult <=0){
            log.error("竞猜项目bet scence 上架失败 id:{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo shelf(int id) {
        int updateResult = betScenceMapper.updateStatus(ShelfStatusEnum.SHELF.getCode(),id);
        if(updateResult <=0){
            log.error("竞猜项目bet scence 下架失败 id:{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }


    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo bet(int userId, int scenceMovieId, BigDecimal amount,String betWhich) {
        User user = userMapper.get(userId);
        if(null == user){
            log.error(" bet user not found ");
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }

        Invest invest = investMapper.findByUserId(userId);
        if(null == invest){
            log.error(" 用户资产不存在，投注 失败 userID:{}",userId);
            return new ResponseVo(ErrorCodeEnum.INVEST_NOT_EXIST,null);
        }

        if(invest.getBalance().add(invest.getRewardBalance()).compareTo(amount) <=0){
            log.error(" 用户余额不足，无法投资,investID:{}",invest.getInvestId());
            return new ResponseVo(ErrorCodeEnum.INVEST_BALANCE_NOT_ENOUGH,null);
        }

        BetScenceMovie betScenceMovie = betScenceMovieMapper.selectByPrimaryKey(scenceMovieId);
        if(null == betScenceMovie){
            log.error(" bet scence movie is null id: {}",scenceMovieId);
            return new ResponseVo(ErrorCodeEnum.SCENCE_MOVIE_NOT_EXIST,null);
        }
        int scenceId = betScenceMovie.getBetScenceId();
        int movieId = betScenceMovie.getBetMovieId();
        BetScence betScence = betScenceMapper.selectByPrimaryKey(scenceId);
        if(null == betScence){
            log.error(" bet scence not found ");
            return new ResponseVo(ErrorCodeEnum.NO_AVALIBLE_SCENCE,null);
        }
        BetMovie betMovie = betMovieMapper.getById(movieId);
        if(null == betMovie){
            log.error(" bet movie not found ");
            return new ResponseVo(ErrorCodeEnum.BET_NO_MOVIE,null);
        }
        BetUser betUser = new BetUser();
        betUser.setBetScenceMovieId(scenceMovieId);
        betUser.setBetWhich(betWhich);
        betUser.setBingo(BingoStatusEnum.WAIT_BINGO.getCode().byteValue());
        betUser.setBetAmount(amount);
        betUser.setUserId(userId);
        betUser.setCreatedTime(new Date());
//        BigDecimal jaFe = BigDecimal.ZERO;
//        BigDecimal reFe = BigDecimal.ZERO;
//        if(betScence.getHasJasckpot() == 1){
//            jaFe = betScence.getJasckpotFee();
//            BigDecimal jasFeeResult = jaFe.multiply(amount);
//            betUser.setBetFee(jasFeeResult);
//        }
//        if(betScence.getHasReservation() == 1){
//            reFe = betScence.getReservationFee();
//            BigDecimal reFeeResult = reFe.multiply(amount);
//            betUser.setReserveFee(reFeeResult);
//        }
        int saveResult = betUserMapper.insertSelective(betUser);
        if(saveResult <=0){
            log.error(" bet user add exception ");
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }

        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(userId);
        balanceStatement.setCreatedTime(new Date());
        balanceStatement.setAmount(amount);
        balanceStatement.setType(FinanceTypeEnum.BET.getCode());
        balanceStatement.setWay(FinanceWayEnum.OUT.getCode());
        BigDecimal betBalance = BigDecimal.ZERO;
        BigDecimal balance = invest.getBalance();
        BigDecimal rewardBalance = invest.getRewardBalance();
        if(balance.compareTo(amount) >=0){
            betBalance = balance.subtract(amount);
            invest.setBalance(betBalance);
        }else{
            betBalance = balance.add(rewardBalance).subtract(amount);
            invest.setBalance(BigDecimal.ZERO);
            invest.setRewardBalance(betBalance);
        }
        int updateRewardResult = investMapper.updateByPrimaryKeySelective(invest);
        if(updateRewardResult<=0){
            log.error(" 更新用户资产失败，投注  userID:{}",userId);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        int saveBalanceStatement = balanceStatementMapper.insertSelective(balanceStatement);
        if(saveBalanceStatement<=0){
            log.error(" 保存余额变动记录，投注  userID:{}",userId);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo getScence(int betType,int userId) {
        BetScence betScence = betScenceMapper.getByBetType(betType);
        BetInfoVo result = new BetInfoVo();
        if(null == betScence){
            return new ResponseVo(ErrorCodeEnum.NO_AVALIBLE_SCENCE,result);
        }
            BeanUtil.copy(result,betScence);
            /** 未开奖的 */
            List<BetMovieInfoVo> movieInfoList = new ArrayList<BetMovieInfoVo>();
            List<BetMovie> movies = betMovieMapper.findAllOnByScenceId(betScence.getId());
            if(movies.size() == 0){
                return new ResponseVo(ErrorCodeEnum.THE_SCENCE_HAS_NO_MOVIE,null);
            }
            for(BetMovie movie : movies){
                BetMovieInfoVo movieResult = new BetMovieInfoVo();
                BeanUtil.copy(movieResult,movie);
                movieResult.setOpenDay(DateUtil.plusDateOneDay(new Date(),1));
                movieResult.setScenceMovieId(betScenceMovieMapper.findIdByScenceAndMovieOn(betScence.getId(),movie.getId()));
                movieResult.setBetMovieCoverUrl(uploadProperties.getUrl(movie.getBetMovieCoverUrl()));
                /**统计票房*/
                BoxOffice boxOffice = boxOfficeMapper.findByDay(DateUtil.getYesTodayAndFormatDay(),movie.getId());
                if(null != boxOffice){
                    movieResult.setBoxInfo(boxOffice.getBoxInfo().setScale(0, RoundingMode.DOWN) + boxOffice.getBoxInfoUnit());
                    movieResult.setSumBoxInfo(boxOffice.getSumBoxInfo().setScale(0, RoundingMode.DOWN)+ boxOffice.getSumBoxInfoUnit());
                    movieResult.setSumDay(boxOffice.getSumDay());
                }

                /**
                 * 统计投注人数
                 * betType @Type BetTypeEnum
                 */
                switch (betType) {
                    case 0:
                        /** 当猜单双时 */
                        BetOddEvenVo oddEvenVo = betUserMapper.countOddEven(betScence.getId(),movie.getId());
                        movieResult.setBetCountVo(oddEvenVo);
                        break;
                    case 1:
                        /** 是否能超过竞猜票房 */
                        BetGuessOverVo guessOverVo = betUserMapper.countGuessOverEven(betScence.getId(),movie.getId());
                        movieResult.setBetCountVo(guessOverVo);
                        break;
                    case 2:
                        /** 选择题 */
                        BetChoiceVo choiceVo = betUserMapper.countChooice(betScence.getId(),movie.getId());
                        movieResult.setBetCountVo(choiceVo);
                        break;
                    case 3:
                        /** 竞猜总票房 */
                        Integer guessTotalBox = betUserMapper.countGuessTotalBox(betScence.getId(),movie.getId());
                        movieResult.setBetCountVo(guessTotalBox);
                        break;
                    default:
                        movieResult.setBetCountVo(null);
                        break;
                }
                movieInfoList.add(movieResult);
            }
            result.setMovieItem(movieInfoList);

            /** 开奖了的 */
            List<BetMovieDrawVo> drawMovieItem = new ArrayList<BetMovieDrawVo>();
            List<BetMovie> drawMovies = betMovieMapper.findAllOffByScenceId(betScence.getId(),DateUtil.getYesTodayAndFormatDay());
            if(drawMovies.size() != 0){
                for(BetMovie betMovie: drawMovies){
                    BetMovieDrawVo drawMovieResult = new BetMovieDrawVo();
                    BeanUtil.copy(drawMovieResult,betMovie);
                    drawMovieResult.setBetMovieCoverUrl(uploadProperties.getUrl(betMovie.getBetMovieCoverUrl()));
                    /**统计票房*/
                    BoxOffice boxOffice = boxOfficeMapper.findByDay(DateUtil.getYesTodayAndFormatDay(),betMovie.getId());
                    if(null == boxOffice){
                        continue;
                    }
                    BetScenceMovie betScenceMovie =  betScenceMovieMapper.findIdByScenceAndMovieOff(betScence.getId(),betMovie.getId(),DateUtil.getYesTodayAndFormatDay());
                    if(null == betScenceMovie){
                        log.error(" 场次不存在 ，跳过, scence  id :{}, movie id :{}",betScence.getId(),betMovie.getId());
                        continue;
                    }
                    drawMovieResult.setStatus(betScenceMovie.getStatus());
                    drawMovieResult.setDrawResult(betScenceMovie.getDrawResult());
                    drawMovieResult.setScenceMovieId(betScenceMovie.getId());
                    drawMovieResult.setBoxInfo(boxOffice.getBoxInfo().setScale(0, RoundingMode.DOWN) + boxOffice.getBoxInfoUnit());
                    drawMovieResult.setSumBoxInfo(boxOffice.getSumBoxInfo().setScale(0, RoundingMode.DOWN) + boxOffice.getSumBoxInfoUnit());
                    drawMovieResult.setSumDay(boxOffice.getSumDay());
                    drawMovieItem.add(drawMovieResult);
                }
                result.setDrawMovieItem(drawMovieItem);
            }

        /** 连续投注 ，奖池*/
        result.setTotalJasckpot(betScence.getTotalReservation().multiply(new BigDecimal("0.5")).setScale(0,BigDecimal.ROUND_DOWN));
        Date now  = new Date();
        String endDay = DateUtil.endDay(now);
        String startDay = DateUtil.getThisWeekMonDay(now);
        List<BetUser> betUsers = betUserMapper.findlastWeekAlreadyBetByUserId(startDay,endDay,userId);

        if(betUsers.size() == 0){
            result.setContinueBetTime(0);
        }else{

            /** 去除同一天的 */
            Map<String,BetUser> needToContinue = new HashMap<String,BetUser>();
            List<BetUser> stored = new ArrayList<BetUser>();
            for(BetUser betUser: betUsers){
                int day = DateUtil.getDayOfMonth(betUser.getCreatedTime());
                if(!needToContinue.containsKey(day+"")){
                    needToContinue.put(day+"",betUser);
                    stored.add(betUser);
                }
            }
            betUsers = stored;
            boolean isContinue = false;
            for(int i = 0 ; i < betUsers.size(); i++){
                if(i+1 < betUsers.size()){
                    if(DateUtil.getSubstractDay(betUsers.get(i+1).getCreatedTime(),betUsers.get(i).getCreatedTime()) == 1){
                        /** 连续竞猜两天 */
                        isContinue = true;
                        if(i+2 < betUsers.size()){
                            if(DateUtil.getSubstractDay(betUsers.get(i+2).getCreatedTime(),betUsers.get(i+1).getCreatedTime()) == 1){
                                /** 连续竞猜三天 */
                                if(i+3 < betUsers.size()){
                                    if(DateUtil.getSubstractDay(betUsers.get(i+3).getCreatedTime(),betUsers.get(i+2).getCreatedTime()) == 1){
                                        /** 连续竞猜四天 */
                                        if(i+4 < betUsers.size()){
                                            if(DateUtil.getSubstractDay(betUsers.get(i+4).getCreatedTime(),betUsers.get(i+3).getCreatedTime()) == 1){
                                                /** 连续竞猜五天*/
                                                if(i+5 < betUsers.size()){
                                                    if(DateUtil.getSubstractDay(betUsers.get(i+5).getCreatedTime(),betUsers.get(i+4).getCreatedTime()) == 1){
                                                        /**连续竞猜六天*/
                                                        if(i+6 < betUsers.size()){
                                                            if(DateUtil.getSubstractDay(betUsers.get(i+6).getCreatedTime(),betUsers.get(i+5).getCreatedTime()) == 1){
                                                                /**连续竞猜七天*/
                                                                log.debug("连续竞猜七天 userId : {}",userId);
                                                                result.setContinueBetTime(7);
                                                                if(i + 7 < betUsers.size()){
                                                                    i = i + 7;
                                                                }
                                                                continue;
                                                            }
                                                        }
                                                        log.debug("连续竞猜六天 userId : {}",userId);
                                                        i = i + 6;
                                                        result.setContinueBetTime(6);
                                                        continue;
                                                    }
                                                }
                                                log.debug("连续竞猜五天 userId : {}",userId);
                                                result.setContinueBetTime(5);
                                                i = i + 5;
                                                continue;
                                            }
                                        }
                                        log.debug("连续竞猜四天 userId : {}",userId);
                                        result.setContinueBetTime(4);
                                        i = i + 4;
                                        continue;
                                    }
                                }
                                log.debug("连续竞猜三天 userId : {}",userId);
                                result.setContinueBetTime(3);
                                i = i + 3;
                                continue;
                            }
                        }
                        log.debug("连续竞猜两天 userId : {}",userId);
                        result.setContinueBetTime(2);
                        i = i + 2;
                        continue;
                    }
                }
            }
            if(!isContinue){
                result.setContinueBetTime(1);
            }
        }
        String monDayBegin = DateUtil.getThisWeekMonDayBegin(now);
        String monDayEnd = DateUtil.getThisWeekMonDayEnd(now);
        BigDecimal continueReward = balanceStatementMapper.sumContinueReward(userId,monDayBegin,monDayEnd);
        result.setContinueBetReward(null != continueReward ? continueReward.setScale(0,BigDecimal.ROUND_DOWN):BigDecimal.ZERO);
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    public ResponseVo getScenceMovie(int scenceMovieId) {
        BetScenceMovie betScenceMovie = betScenceMovieMapper.selectByPrimaryKey(scenceMovieId);
        BetMovieInfoVo result = new BetMovieInfoVo();

        if(null == betScenceMovie){
            log.error(" 查找场次失败 , id : {}",scenceMovieId);
            return new ResponseVo(ErrorCodeEnum.SCENCE_MOVIE_NOT_EXIST,null);
        }
        int scenceId = betScenceMovie.getBetScenceId();
        int movieId = betScenceMovie.getBetMovieId();
        BetMovie betMovie = betMovieMapper.getById(betScenceMovie.getBetMovieId());
        if(null == betMovie){
            log.error(" 查场资电影失败 , scence movie id : {}, movie id :{}",scenceMovieId,movieId);
            return new ResponseVo(ErrorCodeEnum.BET_NO_MOVIE,null);
        }
        BetScence betScence = betScenceMapper.selectByPrimaryKey(scenceId);
        if(null == betScence){
            log.error(" 查场资电影失败 , scence movie id : {}, movie id :{}",scenceMovieId,movieId);
            return new ResponseVo(ErrorCodeEnum.BET_NO_MOVIE,null);
        }
        BoxOffice boxOffice = boxOfficeMapper.findByDay(DateUtil.getYesTodayAndFormatDay(),betMovie.getId());
        if(null != boxOffice){
            log.debug(" 查找票房失败， scence movie id : {}",scenceMovieId);
            result.setBoxInfo(boxOffice.getBoxInfo().setScale(0, RoundingMode.DOWN) + boxOffice.getBoxInfoUnit());
            result.setSumBoxInfo(boxOffice.getSumBoxInfo().setScale(0, RoundingMode.DOWN) + boxOffice.getSumBoxInfoUnit());
        }
        BeanUtil.copy(result,betMovie);
        result.setScenceMovieId(betScenceMovie.getId());
        result.setOpenDay(DateUtil.plusDateOneDay(new Date(),1));
        result.setBetMovieCoverUrl(uploadProperties.getUrl(betMovie.getBetMovieCoverUrl()));
        switch (betScence.getBetType()) {
            case 0:
                /** 当猜单双时 */
                BetOddEvenVo oddEvenVo = betUserMapper.countOddEven(scenceId,movieId);
                result.setBetCountVo(oddEvenVo);
                break;
            case 1:
                /** 是否能超过竞猜票房 */
                BetGuessOverVo guessOverVo = betUserMapper.countGuessOverEven(scenceId,movieId);
                result.setBetCountVo(guessOverVo);
                break;
            case 2:
                /** 选择题 */
                BetChoiceVo choiceVo = betUserMapper.countChooice(scenceId,movieId);
                result.setBetCountVo(choiceVo);
                break;
            case 3:
                /** 竞猜总票房 */
                Integer guessTotalBox = betUserMapper.countGuessTotalBox(scenceId,movieId);
                result.setBetCountVo(guessTotalBox);
                break;
            default:
                result.setBetCountVo(null);
                break;
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    public ResponseVo getMyBetRecord(int userId) {

        BetRecordInfoVo result = new BetRecordInfoVo();

        int checkUserExist = userMapper.checkIfExistByUserId(userId);
        if(checkUserExist <=0){
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }

        List<BetRecordVo> recordVos = new ArrayList<BetRecordVo>();

        List<BetUser> betUsers = betUserMapper.findAllByUserId(userId);
        if(betUsers.size() ==0){
            result.setUserId(userId);
            result.setItems(recordVos);
            result.setContinueBetReward(BigDecimal.ZERO);
            return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
        }

        result.setUserId(userId);

        for(BetUser betUser: betUsers){
            BetRecordVo recordVo = new BetRecordVo();
            recordVo.setId(betUser.getId());
            recordVo.setBetAmount(betUser.getBetAmount().setScale(0));
            BetMovie betMovie = betMovieMapper.findByScenceMovieId(betUser.getBetScenceMovieId());
            if(null == betMovie){
                log.error(" 查找竞猜记录，跳过，查无此电影 scence movie id :{}",betUser.getBetScenceMovieId());
                continue;
            }
            Integer betType = betScenceMapper.getBetTypeByScenceMovieId(betUser.getBetScenceMovieId());
            if(null == betType){
                log.error(" 无此 betType getMyBetRecord scenceMovieID :{}",betUser.getBetScenceMovieId());
                continue;
            }

            BetScenceMovie betScenceMovie = betScenceMovieMapper.findByIdWithoutStatus(betUser.getBetScenceMovieId());
            if(null == betScenceMovie){
                log.error(" 无此场次 ，scence movie id :{}",betUser.getBetScenceMovieId());
                continue;
            }

            BetScence betScence = betScenceMapper.findByIdWithoutStatus(betScenceMovie.getBetScenceId());
            if(null == betScenceMovie){
                log.error(" 项目类型为空 ， scence id :{}",betScenceMovie.getBetScenceId());
                continue;
            }

            BoxOffice boxOffice = boxOfficeMapper.findByDay(DateUtil.dateToStrMatDay(betScenceMovie.getStartDay()),betMovie.getId());
            if(null != boxOffice){
                log.debug(" 票房记录为空，scence movie start day :{}, movie id :{}",DateUtil.dateToStrMatDay(betScenceMovie.getStartDay()),betMovie.getId());
                if(betScence.getBetType().equals(BetTypeEnum.ODD_EVEN.getCode().byteValue())){
                    /** 单双时 */
                    BigDecimal boxInfo = boxOffice.getBoxInfo().setScale(0,BigDecimal.ROUND_DOWN);
                    recordVo.setDrawResultHelper(RegexUtil.getLastNum(boxInfo));
                }else{
                    String boxInfo = boxOffice.getBoxInfo().setScale(0,BigDecimal.ROUND_DOWN)+boxOffice.getBoxInfoUnit();
                    recordVo.setDrawResultHelper(boxInfo);
                }
            }
            recordVo.setBetMovieName(betMovie.getBetMovieName());
            recordVo.setBetAmount(betUser.getBetAmount().setScale(0,BigDecimal.ROUND_HALF_UP));
            recordVo.setBetType(betType);
            recordVo.setBetWhich(betUser.getBetWhich());
            recordVo.setBingo(betUser.getBingo());
            recordVo.setCreatedTime(betUser.getCreatedTime());
            recordVo.setDrawResult(betScenceMovie.getDrawResult());
            recordVo.setOdds(betScenceMovie.getBingoOdds());
            BigDecimal fee = betUser.getBetFee().setScale(0,BigDecimal.ROUND_HALF_UP).add(betUser.getReserveFee()).setScale(0,BigDecimal.ROUND_HALF_UP);
            recordVo.setFee(fee.setScale(0,BigDecimal.ROUND_HALF_UP));
            BigDecimal bingGoPrice = betUser.getBingoPrice().setScale(0,BigDecimal.ROUND_HALF_UP);
            recordVo.setBingoPrice(bingGoPrice);
            if(betUser.getBingo().equals(BingoStatusEnum.CLOSE_RETURNING.getCode().byteValue())){
                /** 赔付，备用金*/
                recordVo.setBingoPrice(betUser.getCloseWithReturning().setScale(0,BigDecimal.ROUND_HALF_UP));
            }
            if(betUser.getBingo().equals(BingoStatusEnum.BINGO.getCode().byteValue())){
                recordVo.setAddedPrice((betUser.getBetAmount().add(betScenceMovie.getBingoOdds().multiply(betUser.getBetAmount()))).setScale(0,BigDecimal.ROUND_HALF_UP));
            }else if(betUser.getBingo().equals(BingoStatusEnum.CLOSE_RETURNING.getCode().byteValue())){
                recordVo.setAddedPrice(betUser.getBetAmount().multiply(new BigDecimal("2")).setScale(0,BigDecimal.ROUND_HALF_UP));
                recordVo.setOdds(BigDecimal.ONE);
            }
            recordVos.add(recordVo);
        }
        result.setItems(recordVos);
        Date now  = new Date();
        String endDay = DateUtil.endDay(now);
        String startDay = DateUtil.startDay(now,7);
        BigDecimal continueReward = balanceStatementMapper.sumContinueReward(userId,startDay,endDay);
        result.setContinueBetReward(null != continueReward ? continueReward.setScale(0,BigDecimal.ROUND_DOWN):BigDecimal.ZERO);
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    public ResponseVo getAll() {
        List<BetScence> result = betScenceMapper.findAllOnLine();
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }
}
