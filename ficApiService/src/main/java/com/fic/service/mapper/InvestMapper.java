package com.fic.service.mapper;

import com.fic.service.entity.Invest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InvestMapper {

    int deleteByPrimaryKey(Integer investId);

    int insert(Invest record);

    int insertSelective(Invest record);

    Invest selectByPrimaryKey(Integer investId);

    int updateByPrimaryKeySelective(Invest record);

    int updateByPrimaryKey(Invest record);

    Invest findByUserId(Integer userId);
}