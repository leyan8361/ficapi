package com.fic.service.mapper;

import com.fic.service.entity.ActorInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ActorInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActorInfo record);

    int insertSelective(ActorInfo record);

    ActorInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActorInfo record);

    int updateByPrimaryKey(ActorInfo record);

    int updateCoverUrl(int id,String roleCoverUrl);

    List<ActorInfo> findAllByMovieId(int movieId);
}