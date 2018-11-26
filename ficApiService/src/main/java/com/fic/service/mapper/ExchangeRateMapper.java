package com.fic.service.mapper;

import com.fic.service.entity.ExchangeRate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeRateMapper {

    int insert(ExchangeRate record);

    int insertSelective(ExchangeRate record);
}