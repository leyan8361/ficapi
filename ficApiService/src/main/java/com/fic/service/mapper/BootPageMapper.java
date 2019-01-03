package com.fic.service.mapper;

import com.fic.service.entity.BootPage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BootPageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BootPage record);

    int insertSelective(BootPage record);

    BootPage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BootPage record);

    int updateByPrimaryKey(BootPage record);

    List<BootPage> findAll();

    int updatePageCoverUrl(int id,String coverUrl);
}