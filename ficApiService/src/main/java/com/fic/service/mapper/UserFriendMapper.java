package com.fic.service.mapper;

import com.fic.service.entity.UserFriend;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFriendMapper {

    int deleteByPrimaryKey(Integer userFriendId);

    int insert(UserFriend record);

    int insertSelective(UserFriend record);

    UserFriend selectByPrimaryKey(Integer userFriendId);

    int updateByPrimaryKeySelective(UserFriend record);

    int updateByPrimaryKey(UserFriend record);
}