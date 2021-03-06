package com.fic.service.mapper;

import com.fic.service.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription:
**/
@Mapper
@Repository
public interface UserMapper {
    /**
     * 保存属性不为空的记录
     */
    int insert(@Param("user") User user);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(@Param("id") Integer id);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKey(@Param("user") User user);

    /**
     * 根据主键查询记录
     */
    User get(@Param("id") Integer id);

    User findByUsername(@Param("username") String username);


    /**
     * 获取最后自增ID
     */
    Integer getLastInsertID();

    Integer checkExistByInviteCode(@Param("inviteCode") String inviteCode);

    User findByInviteCode(@Param("inviteCode") String inviteCode);

    String getUserNameByUserId(@Param("userId") Integer userId);

    Integer checkIfExistByUserId(@Param("userId") Integer userId);

    Integer checkIfExistByTelephone(@Param("telephone") String telephone);

    Integer checkIfExistByEmail(@Param("email") String email);

    List<User> findMissingAddress();

    int updateUserName(@Param("id") Integer id,@Param("telephone") String telephone);

    int updateEmail(@Param("id") Integer id,@Param("email") String email);

    String findTelephoneById(@Param("id") Integer id);
}