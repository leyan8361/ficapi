package com.fic.service.mapper;

import com.fic.service.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    List<TransactionRecord> findAllByType(@Param("condition")Integer condition);

    List<TransactionRecord> findAllByUserIdAndPage(@Param("userId")Integer userId,@Param("offset")Integer offset);

    int updateStatus(@Param("id")int id,@Param("status")int status,@Param("remark")String remark);

    List<TransactionRecord> findAllWaitConfirm();

    int updateStatusForeachList(List<TransactionRecord> recordList);
}