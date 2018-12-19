package com.fic.service.mapper;

import com.fic.service.entity.BoxOffice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface BoxOfficeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BoxOffice record);

    int insertSelective(BoxOffice record);

    BoxOffice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BoxOffice record);

    int updateByPrimaryKey(BoxOffice record);

    BoxOffice findByDay(String sumDay, int movieId);

    List<BoxOffice> findAllByDay(String sumDay);

    List<BoxOffice> countBoxByMovieNameBetween(String startDay,String endDay,int movieId);
}