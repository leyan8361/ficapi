package com.fic.service.scheduled;

import com.fic.service.Enum.PriceEnum;
import com.fic.service.Vo.BetChoiceVo;
import com.fic.service.Vo.BetGuessOverVo;
import com.fic.service.Vo.BetOddEvenVo;
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

            BoxOffice boxOffice = boxOfficeMapper.findByDay(yestToday,null != betMovie?betMovie.getId():null);

            if(null == boxOffice){
                log.error("开奖过程 ，查找票房失败，movie id :{}, day:{}",betScenceMovie.getBetMovieId(),yestToday);
                continue;
            }

            BigInteger boxInfo = boxOffice.getBoxInfo().toBigInteger();


            /**场次 开奖*/
            switch (betScence.getBetType()){
                case 0:
                    /** 当猜单双时 */
                    if(boxInfo.intValue() % 2 == 0){
                        //双
                        betScenceMovie.setDrawResult(PriceEnum.EVEN.getCode()+"");
                    }else{
                        //单
                        betScenceMovie.setDrawResult(PriceEnum.ODD.getCode()+"");
                    }
                    break;
                case 1:
                    /** 是否能超过竞猜票房 */
                    if(StringUtils.isNotEmpty(betScenceMovie.getGuessOverUnit())){
                        boolean isOver = RegexUtil.checkIfOverChieseUnit(boxOffice.getBoxInfoUnit(),boxOffice.getBoxInfo());
                        if(isOver){
                            betScenceMovie.setDrawResult(PriceEnum.CAN.getCode()+"");
                        }else{
                            betScenceMovie.setDrawResult(PriceEnum.COULD_NOT.getCode()+"");
                        }
                    }else{
                        log.error(" 竞猜票房，初级场，无设置竞猜单位 ，无法开奖 scenceMovieId :{}",betScenceMovie.getId());
                    }
                    break;
                case 2:
                    /** 选择题 */
                    if(StringUtils.isNotEmpty(betScenceMovie.getChoiceInput())){

                    }
                    break;
                case 3:
                    /** 竞猜总票房 */

                    break;
                default:
                    break;
            }




            /***/
            for(BetUser betUser : betUsers){
                switch (betScence.getBetType()){
                    case 0:
                        /** 当猜单双时 */
                        //正常情况

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
    }

}
