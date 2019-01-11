package com.fic.service.mapper;

import com.fic.service.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TransactionRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TransactionRecord record);

    int insertSelective(TransactionRecord record);

    TransactionRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TransactionRecord record);

    int updateByPrimaryKey(TransactionRecord record);

    List<TransactionRecord> findAllByType(int status);

    int updateStatus(int id,int status,String remark);

    List<TransactionRecord> findAllWaitConfirm();

    int updateStatusForeachList(List<TransactionRecord> recordList);
}