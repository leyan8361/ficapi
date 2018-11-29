package com.fic.service.mapper;

import com.fic.service.entity.SmsQueue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SmsQueueMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SmsQueue record);

    int insertSelective(SmsQueue record);

    SmsQueue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsQueue record);

    int updateByPrimaryKey(SmsQueue record);

    SmsQueue findByTelephone(String telephone);

    int deleteByTelephone(String telephone);

    int deleteAllBeforeNow();
}