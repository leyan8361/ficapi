package com.fic.service.mapper;

import com.fic.service.entity.BetScenceMovie;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
}