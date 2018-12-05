package com.fic.service.mapper;

import com.fic.service.entity.Distribution;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DistributionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Distribution record);

    int insertSelective(Distribution record);

    Distribution selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Distribution record);

    int updateByPrimaryKey(Distribution record);

    /**
     * 查询二级分销记录
     * @param userId
     * @return
     */
    Distribution findByUserId(Integer userId,Integer inviteUserId,boolean queryType);

    /**
     * 查询父级分销记录
     * @param userId
     * @param fatherUserId
     * @param queryType true 查询注册分销，false查询投资分销
     * @return
     */
    Distribution findByFatherUserId(Integer userId,Integer fatherUserId,boolean queryType);

    List<Distribution> findAllByUserId(Integer userId);
}