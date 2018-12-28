package com.fic.service.scheduled;

import com.fic.service.Enum.*;
import com.fic.service.Vo.*;
import com.fic.service.constants.Constants;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.MaoYanService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.RegexUtil;
import jnr.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Xie
 * @Date 2018/12/18
 * @Discription:
 **/
@Service
public class BetScheduledService {

    private final Logger log = LoggerFactory.getLogger(BetScheduledService.class);

    @Autowired
    MaoYanService maoYanService;
    @Autowired
    BoxOfficeMapper boxOfficeMapper;
    @Autowired
    BetMovieMapper betMovieMapper;
    @Autowired
    BetScenceMovieMapper betScenceMovieMapper;
    @Autowired
    BetUserMapper betUserMapper;
    @Autowired
    BetScenceMapper betScenceMapper;
    @Autowired
    InvestMapper investMapper;
    @Autowired
    BalanceStatementMapper balanceStatementMapper;

    /**
     * 每天 00:08:00触发
     */
    @Scheduled(cron = "0 8 0 * * ?")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void doBoxPull() {
        log.debug(" do Box Pull action !");
        List<BetMovie> betMovieList = betMovieMapper.findAll();
        if (betMovieList.size() <= 0) {
            log.debug(" do Box Pull action no bet movie return !");
            return;
        }
        String sumDay = DateUtil.getYestodayForMaoYan();
        Map<String, BoxOffice> boxOfficesFromMaoYan = maoYanService.getDataByDate(sumDay);
        for (BetMovie movie : betMovieList) {
            String movieName = movie.getBetMovieName();
            if (boxOfficesFromMaoYan.containsKey(movieName)) {
                BoxOffice boxOffice = boxOfficeMapper.findByDay(sumDay, movie.getId());
                BoxOffice maoYanBox = boxOfficesFromMaoYan.get(movieName);
                boolean saveOrUpdate = false;
                if (null == boxOffice) {
                    boxOffice = new BoxOffice();
                    BeanUtil.copy(boxOffice, maoYanBox);
                    boxOffice.setMovieId(movie.getId());
                    saveOrUpdate = true;
                }
                boxOffice.setSumDay(DateUtil.toDayFormatDay_1(sumDay));
                int saveOrUpdateResult = 0;
                if (saveOrUpdate) {
                    //do save
                    saveOrUpdateResult = boxOfficeMapper.insertSelective(boxOffice);
                } else {
                    saveOrUpdateResult = boxOfficeMapper.updateByPrimaryKeySelective(boxOffice);
                }
                if (saveOrUpdateResult <= 0) {
                    log.error(" 更新票房失败 : 电影名称 ：{}， boxOffice : {}", movieName, boxOffice.toString());
                }
            }
        }
    }


    /**
     * 每天 晚上 00:10:00触发
     */
    @Scheduled(cron = "0 10 0 * * ?")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void openPrice() {
        String yestToday = DateUtil.getYesTodayAndFormatDay();
        /** 查找 昨天 场次*/
        List<BetScenceMovie> betScenceMovies = betScenceMovieMapper.findByDate(yestToday);
        if (betScenceMovies.size() == 0) {
            return;
        }
        for (BetScenceMovie betScenceMovie : betScenceMovies) {
            List<BetUser> betUsers = betUserMapper.findByScenceMovieId(betScenceMovie.getId());
            BetScence betScence = betScenceMapper.selectByPrimaryKey(betScenceMovie.getBetScenceId());

            if (null == betScence) {
                log.error("开奖过程 ，查找项目失败，scence id :{}", betScenceMovie.getBetScenceId());
                continue;
            }

            /**根据开始结束时间，场次持续化，生成今天新场资，直到endDay为昨天，不再生成*/
            if (DateUtil.getSubstractDay(DateUtil.minDateOneDay(new Date()),betScenceMovie.getEndDay()) > 0) {
                BetScenceMovie newDayScenceMovie = new BetScenceMovie();
                BeanUtil.copy(newDayScenceMovie, betScenceMovie);
                newDayScenceMovie.setId(null);
                newDayScenceMovie.setStartDay(new Date());
                int saveScenceMovieResult = betScenceMovieMapper.insertSelective(newDayScenceMovie);
                if (saveScenceMovieResult <= 0) {
                    log.error("产生新场次失败，new scence movie :{}", newDayScenceMovie.toString());
                }
            }

            BetMovie betMovie = betMovieMapper.getById(betScenceMovie.getBetMovieId());
            if (null == betMovie) {
                log.error("开奖过程 ，查找电影失败，movie id :{}", betScenceMovie.getBetMovieId());
                continue;
            }

            BoxOffice boxOffice = boxOfficeMapper.findByDay(yestToday, (null != betMovie ? betMovie.getId() : null));

            if (null == boxOffice) {
                log.error("开奖过程 ，查找票房失败，movie id :{}, day:{}", betScenceMovie.getBetMovieId(), yestToday);
                continue;
            }

            /** 处理 场次 开奖结果*/
            doChangeScenceMoviceStatus(betScenceMovie, betScence, boxOffice);
            /** 计算 竞猜用户 奖励*/
            if (betScenceMovie.getStatus().equals(BetScenceMovieStatusEnum.DRAW.getCode().byteValue())) {
                calBetUserReward(betScence, betScenceMovie, betUsers);
            }else if(betScenceMovie.getStatus().equals(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue()) && betScence.getHasReservation() == 1){
            /** 开奖失败 计算用户 退回 */
                doReturning(betScence,betScenceMovie, betUsers);
            }
            int updateScenceMovieResult = betScenceMovieMapper.updateByPrimaryKeySelective(betScenceMovie);
            if (updateScenceMovieResult <= 0) {
                log.error(" 更新 场次 失败， id :{}", betScenceMovie.getId());
                throw new RuntimeException();
            }

        }
    }

    /**
     * 使用备用金赔付 取所有 bet scence 备用金
     */
    private void doReturning(BetScence betScence,BetScenceMovie betScenceMovie, List<BetUser> betUsers) {
        boolean hasRe = false;
        BigDecimal totalReserveAmount = BigDecimal.ZERO;
        BigDecimal totalBetFee = BigDecimal.ZERO;
        BigDecimal jaFee = BigDecimal.ONE;
        BigDecimal reFee = BigDecimal.ONE;
        if (betScence.getHasReservation() == 1) {
            totalReserveAmount = totalReserveAmount.add(betScence.getTotalReservation().multiply(new BigDecimal("0.5")));
            hasRe = true;
            if(betScence.getReservationFee().compareTo(BigDecimal.ZERO) >=0){
                reFee = betScence.getReservationFee();
            }
        }
        if(betScence.getHasJasckpot() ==1 ){
            if(betScence.getJasckpotFee().compareTo(BigDecimal.ZERO) >=0){
                jaFee = betScence.getJasckpotFee();
                totalBetFee = betScence.getTotalJasckpot();
            }
        }

        if (hasRe) {
            /** 实际赔付金额 是需要中奖 投注人本金 * 2 - (投注人本金 * jaFe + 投注人本金 * reFe) */
            BigDecimal totalJaFe = betScenceMovie.getTotalReservationReturning().multiply(jaFee);
            BigDecimal totalReFe = betScenceMovie.getTotalReservationReturning().multiply(reFee);
            BigDecimal principle = betScenceMovie.getTotalReservationReturning();//本金
            BigDecimal totalReserveReturningAmount = principle.add(principle.subtract(totalJaFe).subtract(totalReFe));
            boolean isEnough = false;
            /** 备用金足以赔付 */
            if (totalReserveAmount.compareTo(totalReserveReturningAmount) >= 0) {
                isEnough = true;
            } else {
                isEnough = false;
            }
            BigDecimal totalRealReserve = BigDecimal.ZERO;
            boolean needReturn = false;
            for (BetUser returnUser : betUsers) {
                if (!betScenceMovie.getDrawResult().equals(returnUser.getBetWhich())) {
                    returnUser.setBingo(BingoStatusEnum.UN_BINGO.getCode().byteValue());
                }else{
                    if (!isEnough) {
                        returnUser.setBingo(BingoStatusEnum.CLOSE_RETURNING_EXCEPTION.getCode().byteValue());
                        betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURNING_EXCEPTION.getCode().byteValue());
                    } else {
                        needReturn = true;
                        BigDecimal returning = returnUser.getBetAmount().multiply(new BigDecimal("2"));
                        BigDecimal betFee = returning.multiply(jaFee);
                        BigDecimal reserveFee = returning.multiply(reFee);
                        returnUser.setBetFee(betFee);
                        returnUser.setReserveFee(reserveFee);
                        returnUser.setBingo(BingoStatusEnum.CLOSE_RETURNING.getCode().byteValue());
                        BigDecimal returningFee = returning.subtract(betFee).subtract(reserveFee);
                        totalRealReserve = totalRealReserve.add(returningFee);
                        totalBetFee = totalBetFee.add(betFee);
                        returnUser.setCloseWithReturning(returningFee);
                        generateBalanceAndUpdateInvest(returnUser.getUserId(), returningFee, FinanceTypeEnum.BET_RETURNING.getCode());
                    }
                }
                int updateBetUser = betUserMapper.updateByPrimaryKeySelective(returnUser);
                if (updateBetUser <= 0) {
                    log.error(" 更新 竞猜用户失败 bet user id :{]", returnUser.getId());
                    throw new RuntimeException();
                }
            }
            if(hasRe && needReturn && totalRealReserve.compareTo(BigDecimal.ZERO) >0){
                betScence.setTotalReservation(betScence.getTotalReservation().subtract(totalRealReserve));
                int updateBetScence = betScenceMapper.updateByPrimaryKey(betScence);
                if(updateBetScence <=0){
                    log.error(" 更改退还金额失败，betscence :{}",betScence.toString());
                    throw new RuntimeException();
                }
            }
            if(betScenceMovie.getStatus().equals(BetScenceMovieStatusEnum.CLOSE_RETURNING_EXCEPTION.getCode().byteValue())) {
                int updateBetScenceMovie = betScenceMovieMapper.updateStatus(betScenceMovie.getId(),BetScenceMovieStatusEnum.CLOSE_RETURNING_EXCEPTION.getCode());
                if(updateBetScenceMovie <=0) {
                    log.error(" not enough to returning ,");
                    throw new RuntimeException();
                }
            }
        }
    }

    private void calBetUserReward(BetScence betScence, BetScenceMovie betScenceMovie, List<BetUser> betUsers) {
        BigDecimal odds = betScenceMovie.getBingoOdds();
        boolean hasJa = false;
        boolean hasRe = false;
        BigDecimal jaFee = BigDecimal.ZERO;
        BigDecimal reFee = BigDecimal.ZERO;
        BigDecimal totalJaFee = BigDecimal.ZERO;
        BigDecimal totalReFee = BigDecimal.ZERO;
        if (betScence.getHasJasckpot() == 1) {
            hasJa = true;
            jaFee = betScence.getJasckpotFee();
            totalJaFee = betScence.getTotalJasckpot();
        }
        if (betScence.getHasReservation() == 1) {
            hasRe = true;
            reFee = betScence.getReservationFee();
            totalReFee = betScence.getTotalReservation();
        }
        /** 用户计算奖励 */
        for (BetUser betUser : betUsers) {
            BigDecimal betAmount = betUser.getBetAmount();
            if (betUser.getBetWhich().equals(betScenceMovie.getDrawResult())) {
                /** Bingo */
                //奖金
                BigDecimal oddsReward = betAmount.multiply(odds).setScale(0,BigDecimal.ROUND_DOWN);
                BigDecimal currentJa = BigDecimal.ZERO;
                BigDecimal currentRe = BigDecimal.ZERO;
                if (hasJa) {
                    //手续费
                    currentJa = betAmount.add(oddsReward).multiply(jaFee).setScale(0,BigDecimal.ROUND_DOWN);
                    totalJaFee = totalJaFee.add(currentJa);
                    betScence.setTotalJasckpot(totalJaFee);
                    betUser.setBetFee(currentJa);
                }
                if (hasRe) {
                    //备用金手续费
                    currentRe = betAmount.add(oddsReward).multiply(reFee).setScale(0,BigDecimal.ROUND_DOWN);
                    totalReFee = totalReFee.add(currentRe);
                    betScence.setTotalReservation(totalReFee);
                    betUser.setReserveFee(currentRe);
                }
                BigDecimal resultReward = betAmount.add(oddsReward).subtract(currentJa).subtract(currentRe);
                betUser.setBingoPrice(resultReward);
                betUser.setBingo(BingoStatusEnum.BINGO.getCode().byteValue());

                int updateBetScence = betScenceMapper.updateByPrimaryKeySelective(betScence);

                if(updateBetScence <=0){
                    log.error(" 更新 奖池失败 scence id :{}",betScence.getId());
                    throw new RuntimeException();
                }

                /** 生成 余额变动 */
                generateBalanceAndUpdateInvest(betUser.getUserId(), resultReward, FinanceTypeEnum.BET_REWARD.getCode());

            } else {
                /** NOT Bingo */
                betUser.setBingo(BingoStatusEnum.UN_BINGO.getCode().byteValue());
            }
            int updateUser = betUserMapper.updateByPrimaryKeySelective(betUser);
            if (updateUser <= 0) {
                log.error(" 竞猜 更新用户奖励失败, betUserid :{}", betUser.getId());
                throw new RuntimeException();
            }
        }
    }

    private void doChangeScenceMoviceStatus(BetScenceMovie betScenceMovie, BetScence betScence, BoxOffice boxOffice) {
        BigDecimal odds = BigDecimal.ZERO;
        BigDecimal totalBetAmount = BigDecimal.ZERO;
        /**场次 开奖*/
        switch (betScence.getBetType()) {
            case 0:
                /** 当猜单双时 */
                BigDecimal boxInfo = boxOffice.getBoxInfo();
                boolean odd = true;
                if(boxInfo.equals(BigDecimal.ZERO)){
                    log.error(" 票房为0，开奖异常");
                    break;
                }
                if (boxInfo.setScale(0,BigDecimal.ROUND_DOWN).remainder(new BigDecimal("2")).compareTo(BigDecimal.ZERO) == 0) {
                    //双
                    odd = false;
                    betScenceMovie.setDrawResult(PriceEnum.EVEN.getCode() + "");
                } else {
                    betScenceMovie.setDrawResult(PriceEnum.ODD.getCode() + "");
                }

                /** 赔率 */
                BetOddEvenAmountVo oddEvenAmountVo = betUserMapper.countOddEvenAmount(betScenceMovie.getId());
                if (null == oddEvenAmountVo) {
                    /** 无人投注 */
                    log.debug("无人投注 ，竞猜单双 id:{}", betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                BigDecimal oddTotalAmount = (null != oddEvenAmountVo.getOddFic() ? oddEvenAmountVo.getOddFic() : BigDecimal.ZERO);
                BigDecimal evenTotalAmount = (null != oddEvenAmountVo.getEvenFic() ? oddEvenAmountVo.getEvenFic() : BigDecimal.ZERO);
                if (oddTotalAmount.compareTo(BigDecimal.ZERO) <= 0 && evenTotalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    /** 无人投注 */
                    log.debug("无人投注 ，竞猜单双 id:{}", betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                if (oddEvenAmountVo.getEvenFic().compareTo(BigDecimal.ZERO) <= 0 || oddEvenAmountVo.getOddFic().compareTo(BigDecimal.ZERO) <= 0) {
                    log.error(" 竞猜票房，猜单双，异常情况, 退还 scenceMovieId :{}", betScenceMovie.getId());
                    if (oddTotalAmount.compareTo(BigDecimal.ZERO) <= 0 && evenTotalAmount.compareTo(BigDecimal.ZERO) > 0) {
                        /** 全部双 */
                        totalBetAmount = evenTotalAmount;
                    }
                    if (evenTotalAmount.compareTo(BigDecimal.ZERO) <= 0 && oddTotalAmount.compareTo(BigDecimal.ZERO) > 0) {
                        /** 全部单 */
                        totalBetAmount = oddTotalAmount;
                    }
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    betScenceMovie.setTotalReservationReturning(totalBetAmount);
                    break;
                }
                if (!odd) {
                    /** 赔率 */
                    odds = oddTotalAmount.divide(evenTotalAmount,2);
                } else {
                    //单
                    odds = evenTotalAmount.divide(oddTotalAmount,2);
                }
                betScenceMovie.setBingoOdds(odds);
                betScenceMovie.setStatus(BetScenceMovieStatusEnum.DRAW.getCode().byteValue());
                break;
            case 1:
                /** 是否能超过竞猜票房 */
                if (StringUtils.isEmpty(betScenceMovie.getGuessOverUnit())) {
                    log.error(" 竞猜票房，初级场，无设置竞猜单位 ，无法开奖 scenceMovieId :{}", betScenceMovie.getId());
                    break;
                }

                boolean isOver = RegexUtil.checkIfOverChieseUnit(boxOffice.getBoxInfoUnit(), boxOffice.getBoxInfo());

                if (isOver) {
                    /** 开奖结果*/
                    //能
                    betScenceMovie.setDrawResult(PriceEnum.CAN.getCode() + "");
                } else {
                    //不能
                    betScenceMovie.setDrawResult(PriceEnum.COULD_NOT.getCode() + "");
                }

                /** 赔率 */
                BetGuessOverAmountVo guessOverVo = betUserMapper.countGuessOverAmount(betScenceMovie.getId());
                BigDecimal canAmount = guessOverVo.getCanFic();
                BigDecimal couldntAmount = guessOverVo.getCouldntFic();
                if (null == guessOverVo) {
                    log.error(" 竞猜票房，是否能超过竞猜票房，异常情况, 无法开奖 scenceMovieId :{}", betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                if (canAmount.compareTo(BigDecimal.ZERO) <= 0 && couldntAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    /** 无人投注 */
                    log.error(" 竞猜票房，是否能超过竞猜票房，异常情况, 无人投注， 无法开奖 scenceMovieId :{}", betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                if (canAmount.compareTo(BigDecimal.ZERO) <= 0 && couldntAmount.compareTo(BigDecimal.ZERO) > 0) {
                    /** 全部不能*/
                    totalBetAmount = couldntAmount;
                    betScenceMovie.setTotalReservationReturning(totalBetAmount);
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    break;
                }
                if (couldntAmount.compareTo(BigDecimal.ZERO) <= 0 && canAmount.compareTo(BigDecimal.ZERO) > 0) {
                    /** 全部能 */
                    totalBetAmount = canAmount;
                    betScenceMovie.setTotalReservationReturning(totalBetAmount);
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    break;
                }

                if (isOver) {
                    //能
                    odds = couldntAmount.divide(canAmount,2);
                } else {
                    //不能
                    odds = canAmount.divide(couldntAmount,2);
                }
                betScenceMovie.setBingoOdds(odds);
                betScenceMovie.setStatus(BetScenceMovieStatusEnum.DRAW.getCode().byteValue());
                break;
            case 2:
                /** 选择题 */
                if (StringUtils.isEmpty(betScenceMovie.getChoiceInput())) {
                    log.error(" 竞猜票房，中级场，无设置竞猜单位 ，无法开奖 scenceMovieId :{}", betScenceMovie.getId());
                    break;
                }
                /** 赔率 */
                BetChoiceAmountVo choiceAmountVo = betUserMapper.countChoiceAmount(betScenceMovie.getId());
                BigDecimal aChoiceAmount = choiceAmountVo.getaChoiceAmount();
                BigDecimal bChoiceAmount = choiceAmountVo.getbChoiceAmount();
                BigDecimal cChoiceAmount = choiceAmountVo.getcChoiceAmount();
                BigDecimal dChoiceAmount = choiceAmountVo.getdChoiceAmount();

                int resultChooice = RegexUtil.matchOption(betScenceMovie.getChoiceInput(), boxOffice.getBoxInfo());
                betScenceMovie.setDrawResult(resultChooice + "");
                boolean extremeCondition = false;
                boolean aTag = aChoiceAmount.compareTo(BigDecimal.ZERO) <= 0 ? true : false;
                boolean bTag = bChoiceAmount.compareTo(BigDecimal.ZERO) <= 0 ? true : false;
                boolean cTag = cChoiceAmount.compareTo(BigDecimal.ZERO) <= 0 ? true : false;
                boolean dTag = dChoiceAmount.compareTo(BigDecimal.ZERO) <= 0 ? true : false;
                if (!aTag && bTag && cTag && dTag) extremeCondition = true;
                if (!bTag && aTag && cTag && dTag) extremeCondition = true;
                if (!cTag && aTag && bTag && dTag) extremeCondition = true;
                if (!dTag && aTag && cTag && bTag) extremeCondition = true;
                if (resultChooice == 0 || (aTag && bTag && cTag && dTag)) {
                    log.error(" 竞猜票房，中级场，无人竞猜，设置关闭, 无法开奖 scenceMovieId :{}", betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                if (extremeCondition) {
                    log.error(" 竞猜票房，中级场，异常情况 , 无法开奖 scenceMovieId :{}", betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    break;
                }
                if (resultChooice == PriceEnum.A_CHOICE.getCode() && !(aChoiceAmount.compareTo(BigDecimal.ZERO) <= 0)) {
                    odds = (bChoiceAmount.add(cChoiceAmount).add(dChoiceAmount)).divide(aChoiceAmount,2);
                }
                if (resultChooice == PriceEnum.B_CHOICE.getCode() && !(bChoiceAmount.compareTo(BigDecimal.ZERO) <= 0)) {
                    odds = (aChoiceAmount.add(cChoiceAmount).add(dChoiceAmount)).divide(bChoiceAmount,2);
                }
                if (resultChooice == PriceEnum.C_CHOICE.getCode() && !(cChoiceAmount.compareTo(BigDecimal.ZERO) <= 0)) {
                    odds = (aChoiceAmount.add(bChoiceAmount).add(dChoiceAmount)).divide(cChoiceAmount,2);
                }
                if (resultChooice == PriceEnum.D_CHOICE.getCode() && !(dChoiceAmount.compareTo(BigDecimal.ZERO) <= 0)) {
                    odds = (aChoiceAmount.add(bChoiceAmount).add(cChoiceAmount)).divide(dChoiceAmount,2);
                }
                betScenceMovie.setStatus(BetScenceMovieStatusEnum.DRAW.getCode().byteValue());
                betScenceMovie.setBingoOdds(odds);
                break;
            case 3:
                /** 竞猜总票房 周为单位，
                 * 此为特殊项，统计过去7天票房
                 * condition startDay, endDay
                 * */
                String endDay = DateUtil.dateToStrMatDay(betScenceMovie.getEndDay());
                if (betScenceMovie.getEndDay().compareTo(new Date()) > 0) {
                    /** 周票房竞猜，未满周期，不需要开奖*/
                    log.error(" 竞猜票房，高级场，不需要开奖 scenceMovieId :{}", betScenceMovie.getId());
                    break;
                }
                String startDay = DateUtil.minDateNDay(betScenceMovie.getEndDay(), 7);
                List<BoxOffice> levelThreeForCurrentScenceMovice = boxOfficeMapper.countBoxByMovieNameBetween(startDay, endDay, betScenceMovie.getBetMovieId());
                if (levelThreeForCurrentScenceMovice.size() == 0 || levelThreeForCurrentScenceMovice.size() < 7) {
                    log.error(" 竞猜票房，高级场，票房数据不足一周，无法开奖 scenceMovieId :{}", betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                BigDecimal totalWeekBox = BigDecimal.ZERO;
                for (BoxOffice weekBox : levelThreeForCurrentScenceMovice) {
                    totalWeekBox = totalWeekBox.add(weekBox.getBoxInfo());
                    /**票房单位 为万，竞猜累计为千万，转换*/
                    totalWeekBox = totalWeekBox.divide(new BigDecimal("1000"),2);
                }
                /**
                 * 统计猜不同票房的用户赌资
                 * @Param betAmount 竞猜同一个票房数字的用户，竞猜金额总数
                 * @Param weekBoxAmount 竞猜的票房数字
                 */
                List<BetWeekBoxCountVo> weekBoxCountVoList = betUserMapper.countWeekBoxGroupAmount(betScenceMovie.getId());
                BigDecimal winner = BigDecimal.ZERO;
                BigDecimal loser = BigDecimal.ZERO;
                boolean hasBingo = false;
                if (weekBoxCountVoList.size() == 0) {
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    log.error(" 竞猜票房，高级场，异常情况，无人投注");

                    break;
                }
                if (0 < weekBoxCountVoList.size() && weekBoxCountVoList.size() < 2) {
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    log.error(" 竞猜票房，高级场，异常情况，竞猜人数不足以开奖");
                    updateBetScenceMovie(betScenceMovie);
                    break;
                }
                for (int i = 0; i < weekBoxCountVoList.size(); i++) {
                    if (weekBoxCountVoList.get(i).getWeekBoxAmount().compareTo(totalWeekBox) == 0) {
                        winner = weekBoxCountVoList.get(i).getWeekBoxAmount();
                        hasBingo = true;
                    }
                    loser = loser.add(weekBoxCountVoList.get(i).getBetAmount());
                }
                if (hasBingo) {
                    odds = loser.divide(winner,2);
                    betScenceMovie.setBingoOdds(odds);
                }
                betScenceMovie.setDrawResult(totalWeekBox + "");
                betScenceMovie.setStatus(BetScenceMovieStatusEnum.DRAW.getCode().byteValue());
                break;
            default:
                break;
        }

        updateBetScenceMovie(betScenceMovie);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateBetScenceMovie(BetScenceMovie betScenceMovie) {
        int updateResult = betScenceMovieMapper.updateByPrimaryKeySelective(betScenceMovie);
        if (updateResult <= 0) {
            log.error(" 更新场次失败 ,回滚 ! scencemovieId:{}", betScenceMovie.getId());
            throw new RuntimeException();
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void generateBalanceAndUpdateInvest(int userId, BigDecimal reward, int betStatus) {
        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(userId);
        balanceStatement.setCreatedTime(new Date());
        balanceStatement.setAmount(reward);
        balanceStatement.setType(betStatus);
        balanceStatement.setWay(FinanceWayEnum.IN.getCode());
        Invest invest = investMapper.findByUserId(userId);
        if (null == invest) {
            log.error(" 用户资产不存在，竞猜生成奖励变动 记录失败 userID:{}", userId);
            return;
        }
        int updateRewardResult = investMapper.updateBalance(invest.getBalance().add(reward), userId);
        if (updateRewardResult <= 0) {
            log.error(" 更新用户资产失败，竞猜 userID:{}", userId);
            return;
        }
        int saveBalanceStatement = balanceStatementMapper.insertSelective(balanceStatement);
        if (saveBalanceStatement <= 0) {
            log.error(" 保存余额变动记录，竞猜 userID:{}", userId);
            return;
        }
    }

    /**
     * 周一 00:15:00触发
     */
    @Scheduled(cron = "0 15 0 * * MON")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void rewardPool() {
        /** 查询过去一周，投注次数过至少两次的竞猜用户数据 */
        String endDay = DateUtil.getLastWeekSunDay();
        String startDay = DateUtil.getLastWeekMonDay();
        List<BetContinueBetUserVo> countUserBetContinue = betUserMapper.findLastWeekAlreadyBet(startDay, endDay);
        if (countUserBetContinue.size() == 0) {
            log.error(" 过去7天， 无连续投注用户");
            return;
        }
        /** Key : twoT,threeT,fourT,fiveT ,Value : Integer : userId*/
        Map<String, List<Integer>> rewardMap = new HashMap<String, List<Integer>>();
        rewardMap.put(Constants.TWO_T, new ArrayList<Integer>());
        rewardMap.put(Constants.THREE_T, new ArrayList<Integer>());
        rewardMap.put(Constants.FOUR_T, new ArrayList<Integer>());
        rewardMap.put(Constants.FIVE_T, new ArrayList<Integer>());
        for (BetContinueBetUserVo betCount : countUserBetContinue) {
            int userId = betCount.getUserId();
            List<BetUser> countBetTimeUser = betUserMapper.findlastWeekAlreadyBetByUserId(startDay, endDay, betCount.getUserId());
            if(countBetTimeUser.size() == 0)continue;
            /*** 统计 五次以上 连续竞猜
             *  假设 下注100 次，取2,3,4,5；隔1
             *  如 两次 (100 99),98,(97 96),95,(94 93)
             *  三次(100 99 98),97,(96 95 94),93,(92 91 90)
             *  四次(100 99 98 97),96,(95 94 93 92),91,(90 89 88 87)
             *  五次(100 99 98 97 96),95,(94 93 92 91 90),89,(88 87 86 85 84)
             */
            int betSize = countBetTimeUser.size();
            for(int i = 0 ; i < countBetTimeUser.size(); i++){
                if(i+1 < betSize){
                    if(DateUtil.getSubstractDay(countBetTimeUser.get(i+1).getCreatedTime(),countBetTimeUser.get(i).getCreatedTime()) == 1){
                        /** 连续两次往下 */
                        log.debug("连续两次竞猜 userId :{},id:{}",countBetTimeUser.get(i).getUserId(),countBetTimeUser.get(i).getId());
                        if(i+2 < betSize){
                            if(DateUtil.getSubstractDay(countBetTimeUser.get(i+2).getCreatedTime(),countBetTimeUser.get(i+1).getCreatedTime()) ==1){
                                log.debug("连续三次竞猜 userId :{},id:{}",countBetTimeUser.get(i+2).getUserId(),countBetTimeUser.get(i+1).getId());
                                /** 连续三次往下 */
                                if(i+3 < betSize){
                                    if(DateUtil.getSubstractDay(countBetTimeUser.get(i+3).getCreatedTime(),countBetTimeUser.get(i+2).getCreatedTime()) ==1){
                                        log.debug("连续四次竞猜 userId :{},id:{}",countBetTimeUser.get(i+3).getUserId(),countBetTimeUser.get(i+2).getId());
                                        /** 连续四次往下 */
                                        if(i+4 < betSize){
                                            if(DateUtil.getSubstractDay(countBetTimeUser.get(i+4).getCreatedTime(),countBetTimeUser.get(i+3).getCreatedTime()) == 1){
                                                log.debug("连续五次竞猜 userId :{},id:{}",countBetTimeUser.get(i+4).getUserId(),countBetTimeUser.get(i+3).getId());
                                                /** 连续五次往下 */
                                                rewardMap.get(Constants.FIVE_T).add(userId);
                                                if(i+6 < betSize){
                                                    /** 若未达到size,跳到 94 */
                                                    i = i+6;
                                                }else{
                                                    /** 目标index达到size ,结束 */
                                                    break;
                                                }
                                            }else{
                                                /** 连续四次 继续*/
                                                rewardMap.get(Constants.FOUR_T).add(userId);
                                                if(i+5 < betSize){
                                                    /** 若未达到size,跳到 95 */
                                                    i = i+5;
                                                }else{
                                                    /** 目标index达到size ,结束 */
                                                    break;
                                                }
                                            }
                                            continue;
                                        }else{
                                            /** 连续四次 结束*/
                                            rewardMap.get(Constants.FOUR_T).add(userId);
                                            break;
                                        }
                                    }else{
                                        /** 连续三次 继续*/
                                        rewardMap.get(Constants.THREE_T).add(userId);
                                        if(i+4 < betSize){
                                            /** 若未达到size,跳到 96 */
                                            i = i+4;
                                        }else{
                                            /** 目标index达到size ,结束 */
                                            break;
                                        }
                                        continue;
                                    }
                                }else{
                                    /** 连续三次 结束*/
                                    rewardMap.get(Constants.THREE_T).add(userId);
                                    break;
                                }
                            }else{
                                /** 连续两次 继续*/
                                rewardMap.get(Constants.TWO_T).add(userId);
                                if(i+3 < betSize){
                                    /** 若未达到size,跳到 97 */
                                    i = i+3;
                                }else{
                                    /** 目标index达到size ,结束 */
                                    break;
                                }
                                continue;
                            }
                        }else{
                            /** 连续两次 结束*/
                            rewardMap.get(Constants.TWO_T).add(userId);
                            break;
                        }
                    }
                }
            }
        }

        List<BetScence> betScences = betScenceMapper.findAllOnLine();
        BigDecimal totalRwardAmount = BigDecimal.ZERO;
        BigDecimal twoReward = BigDecimal.ZERO;
        BigDecimal threeReward = BigDecimal.ZERO;
        BigDecimal fourReward = BigDecimal.ZERO;
        BigDecimal fiveReward = BigDecimal.ZERO;
        BigDecimal twoRewardPercent = new BigDecimal(Constants.TWO_PERCENT);
        BigDecimal threeRewardPercent = new BigDecimal(Constants.THREE_PERCENT);
        BigDecimal fourRewardPercent = new BigDecimal(Constants.FOUR_PERCENT);
        BigDecimal fiveRewardPercent = new BigDecimal(Constants.FIVE_PERCENT);
        BigDecimal twoRewardPeopleCount = BigDecimal.ONE;
        if(rewardMap.get(Constants.TWO_T).size() !=0){
            twoRewardPeopleCount = new BigDecimal(rewardMap.get(Constants.TWO_T).size());
        }
        BigDecimal threeRewardPeopleCount = BigDecimal.ONE;
        if(rewardMap.get(Constants.THREE_T).size() !=0){
            threeRewardPeopleCount =  new BigDecimal(rewardMap.get(Constants.THREE_T).size());
        }
        BigDecimal fourRewardPeopleCount = BigDecimal.ONE;
        if(rewardMap.get(Constants.FOUR_T).size() !=0){
            fourRewardPeopleCount =  new BigDecimal(rewardMap.get(Constants.FOUR_T).size());
        }
        BigDecimal fiveRewardPeopleCount = BigDecimal.ONE;
        if(rewardMap.get(Constants.FIVE_T).size() !=0){
            fiveRewardPeopleCount =  new BigDecimal(rewardMap.get(Constants.FIVE_T).size());
        }
        for(BetScence betScence : betScences){
            totalRwardAmount = totalRwardAmount.add(betScence.getTotalReservation().multiply(new BigDecimal("0.5")));
        }
        if(totalRwardAmount.compareTo(BigDecimal.ZERO) <=0){
            log.error(" 奖池为 0 ");
            return;
        }
        twoReward = totalRwardAmount.multiply(twoRewardPercent).divide(twoRewardPeopleCount,2);
        threeReward = totalRwardAmount.multiply(threeRewardPercent).divide(threeRewardPeopleCount,2);
        fourReward = totalRwardAmount.multiply(fourRewardPercent).divide(fourRewardPeopleCount,2);
        fiveReward = totalRwardAmount.multiply(fiveRewardPercent).divide(fiveRewardPeopleCount,2);

        BigDecimal realRewardAmount = BigDecimal.ZERO;
        for(Map.Entry<String,List<Integer>> map: rewardMap.entrySet()){
            BigDecimal rewardAmount = BigDecimal.ZERO;
            for(Integer userId : map.getValue()){
                BalanceStatement balanceStatement = new BalanceStatement();
                balanceStatement.setWay(FinanceWayEnum.IN.getCode());
                balanceStatement.setUserId(userId);
                balanceStatement.setType(FinanceTypeEnum.BET_REWARD_POOL.getCode());
                balanceStatement.setCreatedTime(new Date());
                switch (map.getKey()){
                    case Constants.TWO_T:
                        balanceStatement.setAmount(twoReward);
                        realRewardAmount = realRewardAmount.add(twoReward);
                        rewardAmount = twoReward;
                        break;
                    case Constants.THREE_T:
                        balanceStatement.setAmount(threeReward);
                        realRewardAmount = realRewardAmount.add(threeReward);
                        rewardAmount = threeReward;
                        break;
                    case Constants.FOUR_T:
                        balanceStatement.setAmount(fourReward);
                        realRewardAmount = realRewardAmount.add(fourReward);
                        rewardAmount = fourReward;
                        break;
                    case Constants.FIVE_T:
                        balanceStatement.setAmount(fiveReward);
                        realRewardAmount = realRewardAmount.add(fiveReward);
                        rewardAmount = fiveReward;
                        break;
                    default:
                        break;
                }
                int saveBlanaceResult = balanceStatementMapper.insertSelective(balanceStatement);
                if(saveBlanaceResult <=0){
                    log.error(" 奖池分配失败， 保存balance statement 失败",balanceStatement.toString());
                }
                Invest invest = investMapper.findByUserId(userId);
                if(null == invest){
                    log.error(" reward pool invest not exist, userid:{}",userId);
                    throw new RuntimeException();
                }
                int updateInvest = investMapper.updateBalance(invest.getBalance().add(rewardAmount),userId);
                if(updateInvest <=0){
                    log.error("update invest from reward pool failed. invest id : {}",invest.getInvestId());
                    throw new RuntimeException();
                }
            }
        }

        /** 清空奖池 */
        if(rewardMap.get(Constants.TWO_T).size() > 0 || rewardMap.get(Constants.THREE_T).size() > 0 || rewardMap.get(Constants.FOUR_T).size() >  0 || rewardMap.get(Constants.FIVE_T).size() > 0){

            int updateBetScenceResult = betScenceMapper.updateReservePool(totalRwardAmount.add(totalRwardAmount.subtract(realRewardAmount)),betScences.get(0).getId());
            if(updateBetScenceResult <=0){
                log.error(" 更新 奖池失败 ");
                throw new RuntimeException();
            }
        }
    }


    /**
     * 处理不足够赔付的情况
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void makeUpReturning() {

        List<BetScenceMovie> betScenceMovies = betScenceMovieMapper.findAllWithStatus(BetScenceMovieStatusEnum.CLOSE_RETURNING_EXCEPTION.getCode());

        if(betScenceMovies.size() == 0 ) {
            log.debug(" make up returning no need to returning ");
            return;
        }

        BetScence betScence = betScenceMapper.findByIdWithoutStatus(1);
        if(null == betScence) {
            log.error(" bet scence not found id :{}",1);
            return;
        }

        BigDecimal totalReserveAmount = betScence.getTotalReservation().multiply(new BigDecimal("0.5"));
        BigDecimal totalNeedReturning = BigDecimal.ZERO;
        BigDecimal totalBetFee = BigDecimal.ZERO;
        BigDecimal jaFe = betScence.getJasckpotFee();
        BigDecimal reFe = betScence.getReservationFee();
        for(BetScenceMovie betScenceMovie : betScenceMovies) {
            totalNeedReturning = totalNeedReturning.add(betScenceMovie.getTotalReservationReturning());
        }

        /**
         * 奖池 * 0.5 = 备用金
         * 备用金 - (备用金 * jaFe * reFe) = 实际赔付
         */
        BigDecimal cutFe = totalNeedReturning.multiply(jaFe.add(reFe));
        totalNeedReturning = totalNeedReturning.multiply(new BigDecimal("2")).subtract(cutFe);
        BigDecimal totalRealReturing = BigDecimal.ZERO;
        if(totalReserveAmount.compareTo(totalNeedReturning) <=0) {
            log.error(" not enough to returning totalReserveAmount :{}, totalNeedReturning amount :{}",totalReserveAmount,totalNeedReturning);
            return;
        }

        for(BetScenceMovie betScenceMovie : betScenceMovies) {
            List<BetUser> returnUsers = betUserMapper.findAllWithoutStatusByScenceMovieId(betScenceMovie.getId());
            if(returnUsers.size() == 0) {
                log.debug(" no bet user bet scence movie id : {}",betScenceMovie.getId());
                continue;
            }
            for(BetUser returnUser: returnUsers) {
                if(!returnUser.getBingo().equals(BingoStatusEnum.CLOSE_RETURNING_EXCEPTION.getCode().byteValue())) {
                    continue;
                }
                BigDecimal returning = returnUser.getBetAmount().multiply(new BigDecimal("2"));
                BigDecimal betFee = returning.multiply(jaFe);
                BigDecimal reserveFee = returning.multiply(reFe);
                returnUser.setBetFee(betFee);
                returnUser.setReserveFee(reserveFee);
                returnUser.setBingo(BingoStatusEnum.CLOSE_RETURNING.getCode().byteValue());
                BigDecimal returningFee = returning.subtract(betFee).subtract(reserveFee);
                totalBetFee = totalBetFee.add(betFee);
                totalRealReturing = totalRealReturing.add(returningFee);
                returnUser.setCloseWithReturning(returningFee);
                generateBalanceAndUpdateInvest(returnUser.getUserId(), returningFee, FinanceTypeEnum.BET_RETURNING.getCode());
                int updateBetUser = betUserMapper.updateByPrimaryKeySelective(returnUser);
                if (updateBetUser <= 0) {
                    log.error(" 更新 竞猜用户失败 bet user id :{]", returnUser.getId());
                    throw new RuntimeException();
                }
            }

            int updateBetScenceMovie = betScenceMovieMapper.updateStatus(betScenceMovie.getId(),BetScenceMovieStatusEnum.CLOSE_RETURN.getCode());
            if(updateBetScenceMovie <=0){
                log.error(" 处理betscence movie 赔付失败，betscencemovie id :{}",betScenceMovie.getId());
                throw new RuntimeException();
            }
        }
        betScence.setTotalJasckpot(betScence.getTotalJasckpot().add(totalBetFee));
        betScence.setTotalReservation(betScence.getTotalReservation().subtract(totalRealReturing));
        int updateBetScence = betScenceMapper.updateByPrimaryKey(betScence);
        if(updateBetScence <=0){
            log.error(" 更改退还金额失败，betscence :{}",betScence.toString());
            throw new RuntimeException();
        }
    }

}
