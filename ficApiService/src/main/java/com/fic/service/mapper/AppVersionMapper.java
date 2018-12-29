package com.fic.service.mapper;

import com.fic.service.entity.AppVersion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppVersionMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(AppVersion record);

    int insertSelective(AppVersion record);

    AppVersion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppVersion record);

    int updateByPrimaryKey(AppVersion record);

    AppVersion findByDeviceType(byte deviceType);

    List<AppVersion> findAll();

    int checkByVersionInType(String version,int deviceType);

    int updateFileUrl(String appFilePath, int id);
}