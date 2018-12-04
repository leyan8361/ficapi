package com.fic.service.service;

import com.fic.service.Vo.DistributionVo;

import java.util.List;

/**
 * @Author Xie
 * @Date 2018/12/4
 * @Discription:
 **/
public interface DistributionService {

    List<DistributionVo> getMyDistributionRecord(Integer userId);
}
