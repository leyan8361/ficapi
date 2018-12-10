package com.fic.service.mapper;

import com.fic.service.entity.MovieUserInfo;

public interface MovieUserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MovieUserInfo record);

    int insertSelective(MovieUserInfo record);

    MovieUserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MovieUserInfo record);

    int updateByPrimaryKey(MovieUserInfo record);

    MovieUserInfo findByUserIdAndMovieId(Integer userId,Integer movieId);
}