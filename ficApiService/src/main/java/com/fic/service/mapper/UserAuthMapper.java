package com.fic.service.mapper;

import com.fic.service.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserAuthMapper {

    UserAuth get(int id);

    UserAuth findByUserId(int userId);

    int checkIfExist(int userId);

    int deleteByPrimaryKey(Integer id);

    int insert(UserAuth record);

    int insertSelective(UserAuth record);

    UserAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAuth record);

    int updateByPrimaryKey(UserAuth record);

    List<UserAuth> findAllByStatus(@Param("type") int type);
}