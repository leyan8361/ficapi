package com.fic.service.service.impl;

import com.fic.service.Enum.*;
import com.fic.service.Vo.*;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.BetScenceService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        BigDecimal jaFe = BigDecimal.ZERO;
        BigDecimal reFe = BigDecimal.ZERO;
        if(betScenceMovie.getHasJasckpot().equals((byte)1)){
            jaFe = betScenceMovie.getJasckpotFee();
            BigDecimal jasFeeResult = jaFe.multiply(amount);
            betUser.setBetFee(jasFeeResult);
        }
        if(betScenceMovie.getHasReservation().equals((byte)1)){
            reFe = betScenceMovie.getReservationFee();
            BigDecimal reFeeResult = reFe.multiply(amount);
            betUser.setReserveFee(reFeeResult);
        }
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
    public ResponseVo getScence(int betType) {
        BetScence betScence = betScenceMapper.getByBetType(betType);
        List<BetInfoVo> resultList = new ArrayList<>();
        if(null == betScence){
            return new ResponseVo(ErrorCodeEnum.NO_AVALIBLE_SCENCE,resultList);
        }
            BetInfoVo result = new BetInfoVo();
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
                if(null == boxOffice){
                    continue;
                }
                movieResult.setBoxInfo(boxOffice.getBoxInfo() + boxOffice.getBoxInfoUnit());
                movieResult.setSumBoxInfo(boxOffice.getSumBoxInfo() + boxOffice.getSumBoxInfoUnit());
                movieResult.setSumDay(boxOffice.getSumDay());
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
            List<BetMovie> drawMovies = betMovieMapper.findAllOffByScenceId(betScence.getId());
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
                    drawMovieResult.setScenceMovieId(betScenceMovieMapper.findIdByScenceAndMovieOff(betScence.getId(),betMovie.getId(),DateUtil.getYesTodayAndFormatDay()));
                    drawMovieResult.setBoxInfo(boxOffice.getBoxInfo() + boxOffice.getBoxInfoUnit());
                    drawMovieResult.setSumBoxInfo(boxOffice.getSumBoxInfo() + boxOffice.getSumBoxInfoUnit());
                    drawMovieResult.setSumDay(boxOffice.getSumDay());
                    drawMovieItem.add(drawMovieResult);
                }
                result.setDrawMovieItem(drawMovieItem);
            }

            resultList.add(result);


        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
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
}