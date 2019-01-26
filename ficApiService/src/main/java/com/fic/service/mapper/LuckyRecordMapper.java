package com.fic.service.mapper;

import com.fic.service.entity.LuckyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LuckyRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LuckyRecord record);

    int insertSelective(LuckyRecord record);

    LuckyRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LuckyRecord record);

    int updateByPrimaryKey(LuckyRecord record);

    List<LuckyRecord> findLastTen();

    List<LuckyRecord> findByUserIdWithPage(int userId,int offset,int type);

    int countByBingoPrice(int bingoPrice);

    List<LuckyRecord> findAllByReceive(@Param("whichType") int whichType);

    int updateReceiveById(int id);

    List<LuckyRecord> findAll();
}