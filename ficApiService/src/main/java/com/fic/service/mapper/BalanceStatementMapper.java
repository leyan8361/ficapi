package com.fic.service.mapper;

import com.fic.service.entity.BalanceStatement;

public interface BalanceStatementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BalanceStatement record);

    int insertSelective(BalanceStatement record);

    BalanceStatement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BalanceStatement record);

    int updateByPrimaryKey(BalanceStatement record);
}