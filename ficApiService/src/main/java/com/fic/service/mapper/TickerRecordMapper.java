package com.fic.service.mapper;

import com.fic.service.entity.TickerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TickerRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TickerRecord record);

    int insertSelective(TickerRecord record);

    TickerRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TickerRecord record);

    int updateByPrimaryKey(TickerRecord record);
}