package com.fic.service.mapper;

import com.fic.service.entity.BetScence;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BetScenceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BetScence record);

    int insertSelective(BetScence record);

    BetScence selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BetScence record);

    int updateByPrimaryKey(BetScence record);

    int updateStatus(int status,int id);

    BetScence getByBetType(int betType);

    Integer getBetTypeByScenceMovieId(int scenceMovieId);

    BetScence findByIdWithoutStatus(int id);
}