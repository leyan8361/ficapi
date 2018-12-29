package com.fic.service.mapper;

import com.fic.service.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MovieMapper {

    int deleteByPrimaryKey(Integer movieId);

    int insert(Movie record);

    int insertSelective(Movie record);

    Movie selectByPrimaryKey(Integer movieId);

    int updateByPrimaryKeySelective(Movie record);

    int updateByPrimaryKeyWithBLOBs(Movie record);

    int updateByPrimaryKey(Movie record);

    List<Movie> findAll();

    Integer checkIfExistById(Integer movieId);

    int checkIfExistByName(String movieName);

    int updateMovieCover(String movieCoverUrl, Integer movieId);

    int updateStatus(int status, Integer movieId);
}