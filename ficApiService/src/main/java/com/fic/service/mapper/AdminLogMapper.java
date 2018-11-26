package com.fic.service.mapper;

import com.fic.service.entity.AdminLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminLogMapper {

    int insert(AdminLog record);

    int insertSelective(AdminLog record);
}