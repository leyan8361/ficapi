package com.fic.service.mapper;

import com.fic.service.entity.MovieDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MovieDetailInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MovieDetailInfo record);

    int insertSelective(MovieDetailInfo record);

    MovieDetailInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MovieDetailInfo record);

    int updateByPrimaryKey(MovieDetailInfo record);

    int updateBriefCoverUrl(int id,String briefCoverUrl);

    int updatePlotCoverUrl(int id,String plotCoverUrl);

    MovieDetailInfo findByMovieId(int movieId);
}