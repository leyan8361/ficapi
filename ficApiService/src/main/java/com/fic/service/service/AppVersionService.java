package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;
import org.springframework.web.multipart.MultipartFile;

public interface AppVersionService {

    ResponseVo getAll();

    ResponseVo add(String version, int deviceType, MultipartFile deviceTyappFile);

    ResponseVo update(int id,String version, Integer deviceType, MultipartFile deviceTyappFile);

}
