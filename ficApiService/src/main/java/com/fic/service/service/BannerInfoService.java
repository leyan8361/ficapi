package com.fic.service.service;

import com.fic.service.Vo.BannerInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BannerInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/14
 *   @Discription:
**/
public interface BannerInfoService {

    ResponseVo add(BannerInfoVo bannerInfoVo,MultipartFile bannerFile);

    ResponseVo delete(int id);

    ResponseVo update(BannerInfoVo bannerInfoVo,MultipartFile bannerFile);

    List<BannerInfoVo> getAll();
}
