package com.fic.service.mapper;

import com.fic.service.entity.Wallet;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WalletMapper {

    int deleteByPrimaryKey(Integer walletId);

    int insert(Wallet record);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(Integer walletId);

    int updateByPrimaryKeySelective(Wallet record);

    int updateByPrimaryKey(Wallet record);

    Wallet findByAddressByCompany(int userId);

    List<Wallet> findByUserId(int userId);

    List<Wallet> findAll();

}