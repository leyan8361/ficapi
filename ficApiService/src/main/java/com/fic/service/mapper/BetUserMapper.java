package com.fic.service.mapper;

import com.fic.service.Vo.BetChoiceVo;
import com.fic.service.Vo.BetGuessOverVo;
import com.fic.service.Vo.BetOddEvenVo;
import com.fic.service.entity.BetUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    int checkAlreadyBetUser(int scenceMovieId);

    List<BetUser> findByScenceMovieId(int scenceMovieId);
}