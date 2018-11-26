package com.fic.service.mapper;

import com.fic.service.entity.Movie;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieMapper {

    int deleteByPrimaryKey(Integer movieId);

    int insert(Movie record);

    int insertSelective(Movie record);

    Movie selectByPrimaryKey(Integer movieId);

    int updateByPrimaryKeySelective(Movie record);

    int updateByPrimaryKeyWithBLOBs(Movie record);

    int updateByPrimaryKey(Movie record);
}