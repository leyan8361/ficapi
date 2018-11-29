package com.fic.service.mapper;

import com.fic.service.entity.InvestDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InvestDetailMapper {

    int deleteByPrimaryKey(Integer investDetailId);

    int insert(InvestDetail record);

    int insertSelective(InvestDetail record);

    InvestDetail selectByPrimaryKey(Integer investDetailId);

    int updateByPrimaryKeySelective(InvestDetail record);

    int updateByPrimaryKey(InvestDetail record);

    List<InvestDetail> findByUserId(Integer userId);

    List<Integer> countInvestPeople(Integer movieId);
}