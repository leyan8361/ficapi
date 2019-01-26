package com.fic.service.mapper;

import com.fic.service.entity.BetScenceMovie;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BetScenceMovieMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BetScenceMovie record);

    int insertSelective(BetScenceMovie record);

    BetScenceMovie selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BetScenceMovie record);

    int updateByPrimaryKey(BetScenceMovie record);

    BetScenceMovie findByScenceIdAndMovieId(int scenceId,int movieId);

    BetScenceMovie findByScenceIdAndMovieIdJustYestoDay(int scenceId,int movieId);

    List<BetScenceMovie> findByDate(String yestoday);

    int updateStatus(int id,int status);

    int findIdByScenceAndMovieOn(int scenceId,int movieId);

    BetScenceMovie findByIdWithoutStatus(int id);

    BetScenceMovie findIdByScenceAndMovieOff(int scenceId,int movieId,String startDay);

    List<BetScenceMovie> findAllWithStatus(int status);

    List<BetScenceMovie> getAll();

    List<BetScenceMovie> findAllMovieOn(String startDay);

//    List<BetScenceMovie> findHighLevelByMovieName(String startDay,String endDay);
}