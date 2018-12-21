package com.fic.service.scheduled;

import com.fic.service.Enum.*;
import com.fic.service.Vo.*;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.MaoYanService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.RegexUtil;
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
import java.util.Map;

/**
 *   @Author Xie
 *   @Date 2018/12/18
 *   @Discription:
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

//    @Scheduled(cron = "0 0 0 * * *
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public void doBoxPull() {
        log.debug(" do Box Pull action !");
        List<BetMovie> betMovieList = betMovieMapper.findAll();
        if(betMovieList.size() <=0){
            log.debug(" do Box Pull action no bet movie return !");
            return;
        }
        String sumDay = DateUtil.getYestodayForMaoYan();
        Map<String,BoxOffice> boxOfficesFromMaoYan = maoYanService.getDataByDate(sumDay);
        for(BetMovie movie : betMovieList){
            String movieName = movie.getBetMovieName();
            if(boxOfficesFromMaoYan.containsKey(movieName)){
                BoxOffice boxOffice = boxOfficeMapper.findByDay(sumDay,movie.getId());
                BoxOffice maoYanBox = boxOfficesFromMaoYan.get(movieName);
                boolean saveOrUpdate = false;
                if(null == boxOffice){
                    boxOffice = new BoxOffice();
                    BeanUtil.copy(boxOffice,maoYanBox);
                    boxOffice.setMovieId(movie.getId());
                    saveOrUpdate = true;
                }
                boxOffice.setSumDay(DateUtil.toDayFormatDay_1(sumDay));
                int saveOrUpdateResult = 0;
                if(saveOrUpdate){
                    //do save
                    saveOrUpdateResult = boxOfficeMapper.insertSelective(boxOffice);
                }else{
                    saveOrUpdateResult = boxOfficeMapper.updateByPrimaryKeySelective(boxOffice);
                }
                if(saveOrUpdateResult <=0){
                    log.error(" 更新票房失败 : 电影名称 ：{}， boxOffice : {}",movieName,boxOffice.toString());
                }
            }
        }
    }

    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public void openPrice(){
        String yestToday = DateUtil.getYesTodayAndFormatDay();
        /** 查找 昨天 场次*/
        List<BetScenceMovie> betScenceMovies = betScenceMovieMapper.findByDate(yestToday);
        if(betScenceMovies.size() ==0){
            return;
        }
        for(BetScenceMovie betScenceMovie : betScenceMovies){
            List<BetUser> betUsers = betUserMapper.findByScenceMovieId(betScenceMovie.getId());
            BetScence betScence = betScenceMapper.selectByPrimaryKey(betScenceMovie.getBetScenceId());

            if(null == betScence){
                log.error("开奖过程 ，查找项目失败，scence id :{}",betScenceMovie.getBetScenceId());
                continue;
            }

            /**根据开始结束时间，场次持续化，生成今天新场资，直到endDay为昨天，不再生成*/
            if(betScenceMovie.getEndDay().compareTo(DateUtil.minDateOneDay(new Date())) > 0){
                BetScenceMovie newDayScenceMovie = new BetScenceMovie();
                BeanUtil.copy(newDayScenceMovie,betScenceMovie);
                newDayScenceMovie.setId(null);
                newDayScenceMovie.setStartDay(new Date());
                int saveScenceMovieResult = betScenceMovieMapper.insertSelective(newDayScenceMovie);
                if(saveScenceMovieResult <=0){
                    log.error("产生新场次失败，new scence movie :{}",newDayScenceMovie.toString());
                }
            }

            BetMovie betMovie = betMovieMapper.getById(betScenceMovie.getBetMovieId());
            if(null == betMovie){
                log.error("开奖过程 ，查找电影失败，movie id :{}",betScenceMovie.getBetMovieId());
                continue;
            }

            BoxOffice boxOffice = boxOfficeMapper.findByDay(yestToday,(null != betMovie?betMovie.getId():null));

            if(null == boxOffice){
                log.error("开奖过程 ，查找票房失败，movie id :{}, day:{}",betScenceMovie.getBetMovieId(),yestToday);
                continue;
            }

            /** 处理 场次 开奖结果*/
            doChangeScenceMoviceStatus(betScenceMovie, betScence, boxOffice);
            /** 计算 竞猜用户 奖励*/
            if(betScenceMovie.getStatus().equals(BetScenceMovieStatusEnum.DRAW.getCode().byteValue())){
                calBetUserReward(betScenceMovie, betUsers);
            }
            /** 开奖失败 计算用户 退回 */
            if(betScenceMovie.getStatus().equals(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue())) {
                doReturning(betScenceMovie, betUsers);
            }
            int updateScenceMovieResult = betScenceMovieMapper.updateByPrimaryKeySelective(betScenceMovie);
            if(updateScenceMovieResult <=0){
                log.error(" 更新 场次 失败， id :{}",betScenceMovie.getId());
                throw new RuntimeException();
            }

        }
    }

    private void doReturning(BetScenceMovie betScenceMovie, List<BetUser> betUsers) {
        boolean hasRe = false;
        if(betScenceMovie.getHasReservation().equals((byte)1)){
            hasRe = true;
        }
        if(hasRe){
            BigDecimal totalReserveAmount = (null!=betScenceMovie.getTotalReservation()?betScenceMovie.getTotalReservation():BigDecimal.ZERO);
            BigDecimal totalReserveReturningAmount = (null!=betScenceMovie.getTotalReservationReturning()?betScenceMovie.getTotalReservationReturning():BigDecimal.ZERO);
            boolean isEnough = false;
            /** 备用金足以赔付 */
            if(totalReserveAmount.compareTo(totalReserveReturningAmount) >=0){
                isEnough = true;
            }else{
                isEnough = false;
            }
            for(BetUser returnUser: betUsers){
                if(!isEnough){
                    returnUser.setBingo(BingoStatusEnum.CLOSE_RETURNING_EXCEPTION.getCode().byteValue());
                }else{
                    returnUser.setBingo(BingoStatusEnum.CLOSE_RETURNING.getCode().byteValue());
                    generateBalanceAndUpdateInvest(returnUser.getUserId(),returnUser.getBetAmount(), FinanceTypeEnum.BET_RETURNING.getCode());
                }
                int updateBetUser = betUserMapper.updateByPrimaryKeySelective(returnUser);
                if(updateBetUser <=0){
                    log.error(" 更新 竞猜用户失败 bet user id :{]",returnUser.getId());
                    throw new RuntimeException();
                }
            }
        }
    }

    private void calBetUserReward(BetScenceMovie betScenceMovie, List<BetUser> betUsers) {
        BigDecimal odds = betScenceMovie.getBingoOdds();
        boolean hasJa = false;
        boolean hasRe = false;
        BigDecimal jaFee = BigDecimal.ZERO;
        BigDecimal reFee = BigDecimal.ZERO;betScenceMovie.getReservationFee();
        if(betScenceMovie.getHasJasckpot().equals((byte)1)){
            hasJa = true;
            jaFee = betScenceMovie.getJasckpotFee();
        }
        if(betScenceMovie.getHasReservation().equals((byte)1)){
            hasRe = true;
            reFee = betScenceMovie.getReservationFee();
        }
        /** 用户计算奖励 */
        for(BetUser betUser : betUsers){
            BigDecimal betAmount = betUser.getBetAmount();

            if(betUser.getBetWhich().equals(betScenceMovie.getDrawResult())){
                /** Bingo */
                //奖金
                BigDecimal oddsReward = betAmount.multiply(odds);
                BigDecimal currentJa = BigDecimal.ZERO;
                BigDecimal currentRe = BigDecimal.ZERO;
                if(hasJa){
                    //奖池手续费
                    BigDecimal totalJas = (null!=betScenceMovie.getTotalJasckpot()?betScenceMovie.getTotalJasckpot():BigDecimal.ZERO);
                    currentJa = oddsReward.multiply(jaFee);
                    totalJas = totalJas.add(currentJa);
                    betScenceMovie.setTotalJasckpot(totalJas);
                    betUser.setBetFee(currentJa);
                }
                if(hasRe){
                    //备用金手续费
                    BigDecimal totalRe = (null!=betScenceMovie.getTotalReservation()?betScenceMovie.getTotalReservation():BigDecimal.ZERO);
                    currentRe = oddsReward.multiply(reFee);
                    totalRe = totalRe.add(currentRe);
                    betScenceMovie.setTotalReservation(totalRe);
                    betUser.setReserveFee(currentRe);
                }
                BigDecimal resultReward = betAmount.add(oddsReward.subtract(currentJa).subtract(currentRe));
                betUser.setBingoPrice(resultReward);
                betUser.setBingo(BingoStatusEnum.BINGO.getCode().byteValue());

                /** 生成 余额变动 */
                generateBalanceAndUpdateInvest(betUser.getUserId(),resultReward,FinanceTypeEnum.BET_REWARD.getCode());

            }else{
                /** NOT Bingo */
                betUser.setBingo(BingoStatusEnum.UN_BINGO.getCode().byteValue());
            }
            int updateUser = betUserMapper.updateByPrimaryKeySelective(betUser);
            if(updateUser <=0){
                log.error(" 竞猜 更新用户奖励失败, betUserid :{}",betUser.getId());
                throw new RuntimeException();
            }
        }
    }

    private void doChangeScenceMoviceStatus(BetScenceMovie betScenceMovie, BetScence betScence, BoxOffice boxOffice) {
        BigDecimal odds = BigDecimal.ZERO;
        BigDecimal totalBetAmount = BigDecimal.ZERO;
        /**场次 开奖*/
        switch (betScence.getBetType()){
            case 0:
                /** 当猜单双时 */
                BigInteger boxInfo = boxOffice.getBoxInfo().toBigInteger();
                boolean odd = true;
                if(boxInfo.intValue() % 2 == 0){
                    odd = false;
                    betScenceMovie.setDrawResult(PriceEnum.ODD.getCode()+"");
                }else{
                    //双
                    betScenceMovie.setDrawResult(PriceEnum.EVEN.getCode()+"");
                }

                /** 赔率 */
                BetOddEvenAmountVo oddEvenAmountVo = betUserMapper.countOddEvenAmount(betScenceMovie.getId());
                if(null == oddEvenAmountVo){
                    /** 无人投注 */
                    log.debug("无人投注 ，竞猜单双 id:{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                BigDecimal oddTotalAmount = (null!=oddEvenAmountVo.getOddFic()?oddEvenAmountVo.getOddFic():BigDecimal.ZERO);
                BigDecimal evenTotalAmount = (null!=oddEvenAmountVo.getEvenFic()?oddEvenAmountVo.getEvenFic():BigDecimal.ZERO);
                if(oddTotalAmount.compareTo(BigDecimal.ZERO) <=0 && evenTotalAmount.compareTo(BigDecimal.ZERO) <=0){
                    /** 无人投注 */
                    log.debug("无人投注 ，竞猜单双 id:{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                if(oddEvenAmountVo.getEvenFic().compareTo(BigDecimal.ZERO) <=0  || oddEvenAmountVo.getOddFic().compareTo(BigDecimal.ZERO) <=0 ){
                    log.error(" 竞猜票房，猜单双，异常情况, 退还 scenceMovieId :{}",betScenceMovie.getId());
                    if(oddTotalAmount.compareTo(BigDecimal.ZERO) <=0 && evenTotalAmount.compareTo(BigDecimal.ZERO) > 0){
                        /** 全部双 */
                        totalBetAmount = evenTotalAmount;
                    }
                    if(evenTotalAmount.compareTo(BigDecimal.ZERO) <=0 && oddTotalAmount.compareTo(BigDecimal.ZERO) > 0){
                        /** 全部单 */
                        totalBetAmount = oddTotalAmount;
                    }
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    betScenceMovie.setTotalReservationReturning(totalBetAmount);
                    break;
                }
                if(!odd){
                    /** 赔率 */
                    odds = oddTotalAmount.divide(evenTotalAmount);
                }else{
                    //单
                    odds = evenTotalAmount.divide(oddTotalAmount);
                }
                betScenceMovie.setBingoOdds(odds);
                betScenceMovie.setStatus(BetScenceMovieStatusEnum.DRAW.getCode().byteValue());
                break;
            case 1:
                /** 是否能超过竞猜票房 */
                if(StringUtils.isEmpty(betScenceMovie.getGuessOverUnit())){
                    log.error(" 竞猜票房，初级场，无设置竞猜单位 ，无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    break;
                }

                boolean isOver = RegexUtil.checkIfOverChieseUnit(boxOffice.getBoxInfoUnit(),boxOffice.getBoxInfo());

                if(isOver){
                    /** 开奖结果*/
                    //能
                    betScenceMovie.setDrawResult(PriceEnum.CAN.getCode()+"");
                }else{
                    //不能
                    betScenceMovie.setDrawResult(PriceEnum.COULD_NOT.getCode()+"");
                }

                /** 赔率 */
                BetGuessOverAmountVo guessOverVo = betUserMapper.countGuessOverAmount(betScenceMovie.getId());
                BigDecimal canAmount = guessOverVo.getCanFic();
                BigDecimal couldntAmount = guessOverVo.getCouldntFic();
                if(null == guessOverVo){
                    log.error(" 竞猜票房，是否能超过竞猜票房，异常情况, 无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                if(canAmount.compareTo(BigDecimal.ZERO) <=0 && couldntAmount.compareTo(BigDecimal.ZERO) <=0){
                    /** 无人投注 */
                    log.error(" 竞猜票房，是否能超过竞猜票房，异常情况, 无人投注， 无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                if(canAmount.compareTo(BigDecimal.ZERO) <=0 && couldntAmount.compareTo(BigDecimal.ZERO) >0){
                    /** 全部不能*/
                    totalBetAmount = couldntAmount;
                    betScenceMovie.setTotalReservationReturning(totalBetAmount);
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    break;
                }
                if(couldntAmount.compareTo(BigDecimal.ZERO) <=0 && canAmount.compareTo(BigDecimal.ZERO) >0){
                    /** 全部能 */
                    totalBetAmount = canAmount;
                    betScenceMovie.setTotalReservationReturning(totalBetAmount);
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    break;
                }

                if(isOver){
                    //能
                    odds = couldntAmount.divide(canAmount);
                }else{
                    //不能
                    odds = canAmount.divide(couldntAmount);
                }
                betScenceMovie.setBingoOdds(odds);
                betScenceMovie.setStatus(BetScenceMovieStatusEnum.DRAW.getCode().byteValue());
                break;
            case 2:
                /** 选择题 */
                if(StringUtils.isEmpty(betScenceMovie.getChoiceInput())){
                    log.error(" 竞猜票房，中级场，无设置竞猜单位 ，无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    break;
                }
                /** 赔率 */
                BetChoiceAmountVo choiceAmountVo = betUserMapper.countChoiceAmount(betScenceMovie.getId());
                BigDecimal aChoiceAmount = choiceAmountVo.getaChoiceAmount();
                BigDecimal bChoiceAmount = choiceAmountVo.getbChoiceAmount();
                BigDecimal cChoiceAmount = choiceAmountVo.getcChoiceAmount();
                BigDecimal dChoiceAmount = choiceAmountVo.getdChoiceAmount();

                int resultChooice = RegexUtil.matchOption(betScenceMovie.getChoiceInput(),boxOffice.getBoxInfo());
                betScenceMovie.setDrawResult(resultChooice+"");
                boolean extremeCondition = false;
                boolean aTag = aChoiceAmount.compareTo(BigDecimal.ZERO) <=0 ?true:false;
                boolean bTag = bChoiceAmount.compareTo(BigDecimal.ZERO) <=0 ?true:false;
                boolean cTag = cChoiceAmount.compareTo(BigDecimal.ZERO) <=0 ?true:false;
                boolean dTag = dChoiceAmount.compareTo(BigDecimal.ZERO) <=0 ?true:false;
                if(!aTag && bTag && cTag && dTag)extremeCondition = true;
                if(!bTag && aTag && cTag && dTag)extremeCondition = true;
                if(!cTag && aTag && bTag && dTag)extremeCondition = true;
                if(!dTag && aTag && cTag && bTag)extremeCondition = true;
                if(resultChooice ==0 || (aTag && bTag && cTag && dTag)){
                    log.error(" 竞猜票房，中级场，无人竞猜，设置关闭, 无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                if( extremeCondition){
                    log.error(" 竞猜票房，中级场，异常情况 , 无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    break;
                }
                if(resultChooice == PriceEnum.A_CHOICE.getCode() && !(aChoiceAmount.compareTo(BigDecimal.ZERO) <=0)){
                     odds = (bChoiceAmount.add(cChoiceAmount).add(dChoiceAmount)).divide(aChoiceAmount);
                }
                if(resultChooice == PriceEnum.B_CHOICE.getCode() && !(bChoiceAmount.compareTo(BigDecimal.ZERO) <=0)){
                    odds = (aChoiceAmount.add(cChoiceAmount).add(dChoiceAmount)).divide(bChoiceAmount);
                }
                if(resultChooice == PriceEnum.C_CHOICE.getCode() && !(cChoiceAmount.compareTo(BigDecimal.ZERO) <=0)){
                    odds = (aChoiceAmount.add(bChoiceAmount).add(dChoiceAmount)).divide(cChoiceAmount);
                }
                if(resultChooice == PriceEnum.D_CHOICE.getCode() && !(dChoiceAmount.compareTo(BigDecimal.ZERO) <=0)){
                    odds = (aChoiceAmount.add(bChoiceAmount).add(cChoiceAmount)).divide(dChoiceAmount);
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
                if(betScenceMovie.getEndDay().compareTo(new Date()) >0){
                    /** 周票房竞猜，未满周期，不需要开奖*/
                    log.error(" 竞猜票房，高级场，不需要开奖 scenceMovieId :{}",betScenceMovie.getId());
                    break;
                }
                String startDay = DateUtil.minDateNDay(betScenceMovie.getEndDay(),7);
                List<BoxOffice> levelThreeForCurrentScenceMovice = boxOfficeMapper.countBoxByMovieNameBetween(startDay,endDay,betScenceMovie.getBetMovieId());
                if(levelThreeForCurrentScenceMovice.size() ==0  || levelThreeForCurrentScenceMovice.size() < 7){
                    log.error(" 竞猜票房，高级场，票房数据不足一周，无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    break;
                }
                BigDecimal totalWeekBox = BigDecimal.ZERO;
                for (BoxOffice weekBox : levelThreeForCurrentScenceMovice){
                    totalWeekBox = totalWeekBox.add(weekBox.getBoxInfo());
                    /**票房单位 为万，竞猜累计为千万，转换*/
                    totalWeekBox = totalWeekBox.divide(new BigDecimal("1000"));
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
                if(weekBoxCountVoList.size() == 0){
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_NO_BET.getCode().byteValue());
                    log.error(" 竞猜票房，高级场，异常情况，无人投注");

                    break;
                }
                if(0 < weekBoxCountVoList.size() && weekBoxCountVoList.size() < 2){
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE_RETURN.getCode().byteValue());
                    log.error(" 竞猜票房，高级场，异常情况，竞猜人数不足以开奖");
                    updateBetScenceMovie(betScenceMovie);
                    break;
                }
                for(int i = 0 ; i < weekBoxCountVoList.size(); i++){
                    if(weekBoxCountVoList.get(i).getWeekBoxAmount().compareTo(totalWeekBox) ==0){
                        winner = weekBoxCountVoList.get(i).getWeekBoxAmount();
                        hasBingo = true;
                    }
                    loser = loser.add(weekBoxCountVoList.get(i).getBetAmount());
                }
                if(hasBingo){
                    odds = loser.divide(winner);
                    betScenceMovie.setBingoOdds(odds);
                }
                betScenceMovie.setDrawResult(totalWeekBox+"");
                betScenceMovie.setStatus(BetScenceMovieStatusEnum.DRAW.getCode().byteValue());
                updateBetScenceMovie(betScenceMovie);
                break;
            default:
                break;
        }

        updateBetScenceMovie(betScenceMovie);
    }

    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateBetScenceMovie(BetScenceMovie betScenceMovie) {
            int updateResult = betScenceMovieMapper.updateByPrimaryKeySelective(betScenceMovie);
            if(updateResult <=0){
                log.error(" 更新场次失败 ,回滚 ! scencemovieId:{}",betScenceMovie.getId());
                throw new RuntimeException();
            }
    }


    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public void generateBalanceAndUpdateInvest(int userId,BigDecimal reward,int betStatus){
        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(userId);
        balanceStatement.setCreatedTime(new Date());
        balanceStatement.setAmount(reward);
        balanceStatement.setType(betStatus);
        balanceStatement.setWay(FinanceWayEnum.IN.getCode());
        Invest invest = investMapper.findByUserId(userId);
        if(null == invest){
            log.error(" 用户资产不存在，竞猜生成奖励变动 记录失败 userID:{}",userId);
            return;
        }
        int updateRewardResult = investMapper.updateBalance(invest.getBalance().add(reward),userId);
        if(updateRewardResult<=0){
            log.error(" 更新用户资产失败，竞猜 userID:{}",userId);
            return;
        }
        int saveBalanceStatement = balanceStatementMapper.insertSelective(balanceStatement);
        if(saveBalanceStatement<=0){
            log.error(" 保存余额变动记录，竞猜 userID:{}",userId);
            return;
        }
    }

}
