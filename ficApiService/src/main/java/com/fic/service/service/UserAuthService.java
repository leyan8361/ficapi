package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.UserAuth;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserAuthService {

        List<UserAuth> findAllByStatus(int type);

        ResponseVo findByUserId(int userId);

        ResponseVo add(int userId, String cerId, String name, MultipartFile frontFaceFile,MultipartFile backFaceFile);

        ResponseVo update(int userAuthId,String cerId, String name, MultipartFile frontFaceFile,MultipartFile backFaceFile);

        ResponseVo auditing(int userAuthId,boolean pass,String remark);
}
