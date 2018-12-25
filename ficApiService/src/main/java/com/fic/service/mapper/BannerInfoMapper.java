package com.fic.service.mapper;

import com.fic.service.entity.BannerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BannerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BannerInfo record);

    int insertSelective(BannerInfo record);

    BannerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BannerInfo record);

    int updateByPrimaryKey(BannerInfo record);

    int updateBannerUrl(String bannerUrl,int id);

    List<BannerInfo> getAll();
}