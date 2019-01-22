package com.fic.service.service;


import com.fic.service.Vo.LuckTurntableAddVo;
import com.fic.service.Vo.LuckTurntableUpdateVo;
import com.fic.service.Vo.ResponseVo;
import org.springframework.web.multipart.MultipartFile;

public interface LuckTurntableService {

    ResponseVo getAll();

    ResponseVo add(LuckTurntableAddVo luckTurntableAddVo);

    ResponseVo update(LuckTurntableUpdateVo luckTurntableUpdateVo);

    ResponseVo delete(int id);

    ResponseVo uploadCoverFile(MultipartFile coverFile);

    /** api */
    ResponseVo getPrice();

    /** api */
    ResponseVo getBingoRecord(int userId,int pageNum);

    /** api */
    ResponseVo draw(Integer userId,Integer priceId,String word);

    /** api */
    ResponseVo receive(Integer userId,Integer recordId);

}
