package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BootPage;
import org.springframework.web.multipart.MultipartFile;

public interface BootPageService {

    ResponseVo getAll();

    ResponseVo getPages();

    ResponseVo add(BootPage bootPage, MultipartFile pageCoverFile);

    ResponseVo update(BootPage bootPage, MultipartFile pageCoverFile);

    ResponseVo delete(int id);
}
