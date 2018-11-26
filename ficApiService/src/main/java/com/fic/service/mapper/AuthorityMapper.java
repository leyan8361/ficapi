package com.fic.service.mapper;

import com.fic.service.entity.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Mapper
@Repository
public interface AuthorityMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Authority record);

    int insertSelective(Authority record);

    Authority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Authority record);

    int updateByPrimaryKey(Authority record);

    Set<Authority> findAll();

    Set<Authority> findByUsername(String username);
}