package com.fic.service.mapper;

import com.fic.service.entity.MovieMedia;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MovieMediaMapper {

    int deleteByPrimaryKey(Integer movieMediaId);

    int insert(MovieMedia record);

    int insertSelective(MovieMedia record);

    MovieMedia selectByPrimaryKey(Integer movieMediaId);

    int updateByPrimaryKeySelective(MovieMedia record);

    int updateByPrimaryKey(MovieMedia record);

    MovieMedia findByMovieId(int movieId);
}