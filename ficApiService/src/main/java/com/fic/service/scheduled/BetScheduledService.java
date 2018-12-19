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
import org.springframework.scheduling.annotation.Scheduled;
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
        String sumDay = DateUtil.getYesterdayAndFormatDay();
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
        String yestToday = DateUtil.getYesterdayAndFormatDay();
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
            if(betScenceMovie.getEndDay().compareTo(DateUtil.minDateOneDay(new Date())) <=0){
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
                calBetUserReward(betScenceMovie, betUsers, betScence);
            }


        }
    }

    private void calBetUserReward(BetScenceMovie betScenceMovie, List<BetUser> betUsers, BetScence betScence) {
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
            switch (betScence.getBetType()){
                case 0:
                    /** 当猜单双时 */
                    if(betScenceMovie.getStatus().equals(BetScenceMovieStatusEnum.CLOSE.getCode().byteValue())){
                        /** 异常情况 */
                        //TODO
                        break;
                    }
                    if(!betScenceMovie.getStatus().equals(BetScenceMovieStatusEnum.DRAW.getCode().byteValue())){
                        log.error(" 竞猜票房，猜单双，场次状态显示未开奖， 无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                        break;
                    }
                    if(betUser.getBetWhich().equals(betScenceMovie.getDrawResult())){
                        /** Bingo */
                        BigDecimal reward = BigDecimal.ZERO;
                        BigDecimal betAmount = betUser.getBetAmount();

                    }
                    break;
                case 1:
                    /** 是否能超过竞猜票房 */

                    break;
                case 2:
                    /** 选择题 */

                    break;
                case 3:
                    /** 竞猜总票房 */

                    break;
                default:
                    break;
            }
        }
    }

    private void doChangeScenceMoviceStatus(BetScenceMovie betScenceMovie, BetScence betScence, BoxOffice boxOffice) {
        BigDecimal odds = BigDecimal.ZERO;
        /**场次 开奖*/
        switch (betScence.getBetType()){
            case 0:
                /** 当猜单双时 */
                BigInteger boxInfo = boxOffice.getBoxInfo().toBigInteger();
                boolean odd = true;
                if(boxInfo.intValue() % 2 == 0){
                    odd = false;
                }

                /** 赔率 */
                BetOddEvenAmountVo oddEvenAmountVo = betUserMapper.countOddEvenAmount(betScenceMovie.getId());
                if(null == oddEvenAmountVo || oddEvenAmountVo.getEvenFic().equals(BigDecimal.ZERO) || oddEvenAmountVo.getOddFic().equals(BigDecimal.ZERO)){
                    log.error(" 竞猜票房，猜单双，异常情况, 无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE.getCode().byteValue());
                    break;
                }
                BigDecimal oddTotalAmount = oddEvenAmountVo.getOddFic();
                BigDecimal evenTotalAmount = oddEvenAmountVo.getEvenFic();
                if(!odd){
                    //双
                    betScenceMovie.setDrawResult(PriceEnum.EVEN.getCode()+"");
                    /** 赔率 */
                    odds = oddTotalAmount.divide(evenTotalAmount);
                }else{
                    //单
                    betScenceMovie.setDrawResult(PriceEnum.ODD.getCode()+"");
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

                /** 赔率 */
                BetGuessOverVo guessOverVo = betUserMapper.countGuessOverEven(betScenceMovie.getBetScenceId(),betScenceMovie.getBetMovieId());
                if(null == guessOverVo || guessOverVo.getCanCount().equals(0) || guessOverVo.getCouldntCount().equals(0)){
                    log.error(" 竞猜票房，猜单双，异常情况, 无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE.getCode().byteValue());
                    break;
                }
                BigDecimal canNum = new BigDecimal(guessOverVo.getCanCount());
                BigDecimal couldntNum = new BigDecimal(guessOverVo.getCouldntCount());
                boolean isOver = RegexUtil.checkIfOverChieseUnit(boxOffice.getBoxInfoUnit(),boxOffice.getBoxInfo());
                if(isOver){
                    //能
                    betScenceMovie.setDrawResult(PriceEnum.CAN.getCode()+"");
                    odds = couldntNum.divide(canNum);
                }else{
                    //不能
                    betScenceMovie.setDrawResult(PriceEnum.COULD_NOT.getCode()+"");
                    odds = canNum.divide(couldntNum);
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
                if(resultChooice ==0 || (
                                aChoiceAmount.equals(BigDecimal.ZERO) &&
                                bChoiceAmount.equals(BigDecimal.ZERO) &&
                                cChoiceAmount.equals(BigDecimal.ZERO) &&
                                dChoiceAmount.equals(BigDecimal.ZERO)
                )){
                    log.error(" 竞猜票房，中级场，无人竞猜，设置关闭, 无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    betScenceMovie.setStatus(BetScenceMovieStatusEnum.CLOSE.getCode().byteValue());
                    break;
                }
                if(resultChooice == PriceEnum.A_CHOICE.getCode() && !aChoiceAmount.equals(BigDecimal.ZERO)){
                     odds = (bChoiceAmount.add(cChoiceAmount).add(dChoiceAmount)).divide(aChoiceAmount);
                }
                if(resultChooice == PriceEnum.B_CHOICE.getCode() && !bChoiceAmount.equals(BigDecimal.ZERO)){
                    odds = (aChoiceAmount.add(cChoiceAmount).add(dChoiceAmount)).divide(bChoiceAmount);
                }
                if(resultChooice == PriceEnum.C_CHOICE.getCode() && !cChoiceAmount.equals(BigDecimal.ZERO)){
                    odds = (aChoiceAmount.add(bChoiceAmount).add(dChoiceAmount)).divide(cChoiceAmount);
                }
                if(resultChooice == PriceEnum.D_CHOICE.getCode() && !dChoiceAmount.equals(BigDecimal.ZERO)){
                    odds = (aChoiceAmount.add(bChoiceAmount).add(cChoiceAmount)).divide(dChoiceAmount);
                }
                betScenceMovie.setStatus(BetScenceMovieStatusEnum.DRAW.getCode().byteValue());
                betScenceMovie.setDrawResult(resultChooice+"");
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
                break;
            default:
                break;
        }
    }

    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public void generateBalanceAndUpdateInvest(int userId,BigDecimal reward){
        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(userId);
        balanceStatement.setCreatedTime(new Date());
        balanceStatement.setAmount(reward);
        balanceStatement.setType(FinanceTypeEnum.BET_REWARD.getCode());
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
