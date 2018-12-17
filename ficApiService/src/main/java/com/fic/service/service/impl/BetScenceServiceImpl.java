package com.fic.service.service.impl;

import com.fic.service.Enum.BetTypeEnum;
import com.fic.service.Enum.BingoStatusEnum;
import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.ShelfStatusEnum;
import com.fic.service.Vo.*;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.BetScenceService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(BetScenceVo betScenceVo) {
        if(null == betScenceVo){
            log.error(" add bet Scence失败  scence is null");
            return new ResponseVo(ErrorCodeEnum.BET_ADD_MOVIE_NOT_FOUND,null);
        }
        BetScence betScence = new BetScence();
        BeanUtil.copy(betScence,betScenceVo);
        int addResult = betScenceMapper.insertSelective(betScence);
        if(addResult <=0){
            log.error(" add bet Scence 失败 ");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo addMovie(int id, int movieId) {
        BetScence scence = betScenceMapper.selectByPrimaryKey(id);
        if(null == scence){
            return new ResponseVo(ErrorCodeEnum.NO_AVALIBLE_SCENCE,null);
        }
        BetMovie movie = betMovieMapper.getById(movieId);
        if(null == movie){
            return new ResponseVo(ErrorCodeEnum.BET_ADD_MOVIE_NOT_FOUND,null);
        }
        BetScenceMovie scenceMovie = new BetScenceMovie();
        scenceMovie.setBetScenceId(id);
        scenceMovie.setBetMovieId(movieId);
        int saveResult = betScenceMovieMapper.insertSelective(scenceMovie);
        if(saveResult <=0){
            log.error(" 为scence 新增movie 失败 ");
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
    public ResponseVo bet(int userId, int scenceId, int movieId, BigDecimal amount,String betWhich) {
        User user = userMapper.get(userId);
        if(null == user){
            log.error(" bet user not found ");
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }
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
        BetScenceMovie scenceMovie = betScenceMovieMapper.findByScenceIdAndMovieId(scenceId,movieId);
        if(null == scenceMovie){
            log.error(" bet scence movie not found ");
            return new ResponseVo(ErrorCodeEnum.THE_MOVIE_NOT_IN_SCENCE,null);
        }

        BetUser betUser = new BetUser();
        betUser.setBetScenceMovieId(scenceMovie.getId());
        betUser.setBetWhich(betWhich);
        betUser.setBingo(BingoStatusEnum.WAIT_BINGO.getCode());
        betUser.setBingoPrice(amount);
        betUser.setUserId(userId);

        int saveResult = betUserMapper.insertSelective(betUser);
        if(saveResult <=0){
            log.error(" bet user add exception ");
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo getScence(int betType) {
        List<BetScence> findResult = betScenceMapper.getByBetType(betType);
        List<BetInfoVo> resultList = new ArrayList<>();
        if(findResult.size() ==0){
            return new ResponseVo(ErrorCodeEnum.NO_AVALIBLE_SCENCE,resultList);
        }
        for(BetScence betScence: findResult){
            BetInfoVo result = new BetInfoVo();
            BeanUtil.copy(result,betScence);
            /** 未开奖的 */
            List<BetMovieInfoVo> movieInfoList = new ArrayList<BetMovieInfoVo>();
            List<BetMovie> movies = betMovieMapper.findAllOnByScenceId(betScence.getId());
            if(movies.size() == 0){
                continue;
            }
            for(BetMovie movie : movies){
                BetMovieInfoVo movieResult = new BetMovieInfoVo();
                BeanUtil.copy(movieResult,movie);
                movieResult.setBetMovieCoverUrl(uploadProperties.getUrl(movie.getBetMovieCoverUrl()));
                /**统计票房*/
                BoxOffice boxOffice = boxOfficeMapper.findByDay(DateUtil.getYesterdayAndFormatDay(),movie.getId());
                if(null == boxOffice){
                    continue;
                }
                movieResult.setBoxInfo(boxOffice.getBoxInfo());
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
            if(drawMovies.size() == 0){
                continue;
            }
            for(BetMovie betMovie: drawMovies){
                BetMovieDrawVo drawMovieResult = new BetMovieDrawVo();
                BeanUtil.copy(drawMovieResult,betMovie);
                drawMovieResult.setBetMovieCoverUrl(uploadProperties.getUrl(betMovie.getBetMovieCoverUrl()));
                /**统计票房*/
                BoxOffice boxOffice = boxOfficeMapper.findByDay(DateUtil.getYesterdayAndFormatDay(),betMovie.getId());
                if(null == boxOffice){
                    continue;
                }
                drawMovieResult.setBoxInfo(boxOffice.getBoxInfo());
                drawMovieResult.setSumBoxInfo(boxOffice.getSumBoxInfo() + boxOffice.getSumBoxInfoUnit());
                drawMovieResult.setSumDay(boxOffice.getSumDay());
                drawMovieItem.add(drawMovieResult);
            }
            result.setDrawMovieItem(drawMovieItem);

            resultList.add(result);
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }
}
