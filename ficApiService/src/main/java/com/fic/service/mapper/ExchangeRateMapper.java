package com.fic.service.mapper;

import com.fic.service.entity.ExchangeRate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExchangeRateMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeRate record);

    int insertSelective(ExchangeRate record);

    ExchangeRate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeRate record);

    int updateByPrimaryKey(ExchangeRate record);

    ExchangeRate findFicExchangeCny();

    List<ExchangeRate> findAll();

    int findIfExistSame(String coin1,String coin2);
}