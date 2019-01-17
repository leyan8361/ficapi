package com.fic.service.mapper;

import com.fic.service.entity.LuckyTurntable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LuckyTurntableMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LuckyTurntable record);

    int insertSelective(LuckyTurntable record);

    LuckyTurntable selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LuckyTurntable record);

    int updateByPrimaryKey(LuckyTurntable record);

    List<LuckyTurntable> findAll();

    LuckyTurntable get(int id);

    int updateCover(String coverUrl);
}