package com.fic.service.mapper;

import com.fic.service.Vo.*;
import com.fic.service.entity.BetUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface BetUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BetUser record);

    int insertSelective(BetUser record);

    BetUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BetUser record);

    int updateByPrimaryKey(BetUser record);

    BetOddEvenVo countOddEven(int scenceId,int movieId);

    BetGuessOverVo countGuessOverEven(int scenceId,int movieId);

    BetChoiceVo countChooice(int scenceId,int movieId);

    Integer countGuessTotalBox(int scenceId,int movieId);

    BetOddEvenAmountVo countOddEvenAmount(int scenceMovieId);

    BetGuessOverAmountVo countGuessOverAmount(int scenceMovieId);

    BetChoiceAmountVo countChoiceAmount(int scenceMovieId);

    List<BetWeekBoxCountVo> countWeekBoxGroupAmount(int scenceMovieId);

    int checkAlreadyBetUser(int scenceMovieId);

    List<BetUser> findByScenceMovieId(int scenceMovieId);

    List<BetUser> findAllByUserId(int userId);

    List<BetContinueBetUserVo> findLastWeekAlreadyBet(String startDay,String endDay);

    List<BetUser> findlastWeekAlreadyBetByUserId(String startDay,String endDay,int userId);

    List<BetUser> findByScenceMovieAndNotReturing(int scenceMovieId,int status);

    List<BetUser> findAllNotReturning();

    List<BetUser> findAllWithoutStatusByScenceMovieId(int scenceMovieId);

    List<BetUser> findByBingoPriceAndUserId(BigDecimal bingoPrice,int userId,String startDay,String endDay,String... ids);

    List<BetUser> findByReturningAndUserId(BigDecimal returning,int userId,String startDay,String endDay,String... ids);

    BetUser findByBetAmountAndUserIdAndCreatedTime(BigDecimal amount, int userId, String createdTime);

    String findMovieNameById(int id);

    List<BetUser> findLastWinner();

    List<Integer> findBetRanking(String startDay,String endDay);
}