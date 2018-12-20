package com.fic.service.mapper;

import com.fic.service.entity.BetMovie;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BetMovieMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BetMovie record);

    int insertSelective(BetMovie record);

    BetMovie selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BetMovie record);

    int updateByPrimaryKey(BetMovie record);

    int checkExistMovieName(String movieName);

    int updateMovieCoverURL(String movieCoverURL, int id);

    int updateStatus(int status, int id);

    List<BetMovie> findAll();

    BetMovie getById(int id);

    List<BetMovie> findAllOnByScenceId(int scenceId);

    List<BetMovie> findAllOffByScenceId(int scenceId);

    BetMovie findByScenceMovieId(int scenceMovieId);

    int checkMovieNameExistAndStatus(String movieName);
}