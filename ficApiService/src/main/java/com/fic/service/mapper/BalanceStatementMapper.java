package com.fic.service.mapper;

import com.fic.service.entity.BalanceStatement;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BalanceStatementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BalanceStatement record);

    int insertSelective(BalanceStatement record);

    BalanceStatement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BalanceStatement record);

    int updateByPrimaryKey(BalanceStatement record);

    List<BalanceStatement> findAllByUserId(Integer userId);
}