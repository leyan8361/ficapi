package com.fic.service.mapper;

import com.fic.service.entity.WxPayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WxPayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxPayInfo record);

    int insertSelective(WxPayInfo record);

    WxPayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxPayInfo record);

    WxPayInfo findByOrderNum(String orderNum);

    WxPayInfo findByRefundNo(String refundNo);

    List<WxPayInfo> findAllByPayStatus(Integer status);

    List<WxPayInfo> findAllByRefundStatus(Integer status);
}