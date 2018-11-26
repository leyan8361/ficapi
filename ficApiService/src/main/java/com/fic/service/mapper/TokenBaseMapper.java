package com.fic.service.mapper;

import com.fic.service.entity.TokenBase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TokenBaseMapper {

    int deleteByPrimaryKey(Integer userId);

    int insert(TokenBase record);

    int insertSelective(TokenBase record);

    TokenBase selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(TokenBase record);

    int updateByPrimaryKey(TokenBase record);

    TokenBase findByTokenValue(String token);

    int deleteByTokenValue(String token);
}