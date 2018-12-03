package com.fic.service.mapper;

import com.fic.service.entity.DistributionRecord;

public interface DistributionRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributionRecord record);

    int insertSelective(DistributionRecord record);

    DistributionRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributionRecord record);

    int updateByPrimaryKey(DistributionRecord record);
}