package com.fic.service.service;

import com.fic.service.Vo.BetMovieVo;
import com.fic.service.Vo.BetScenceVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetMovie;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Xie
 * @Date 2018/12/17
 * @Discription:
 **/
public interface BetMovieService {

    ResponseVo getMovieOn(int betType);

    ResponseVo getHistory(int betType);

    ResponseVo getById(int movieId);

    ResponseVo add(BetMovie betMovie, MultipartFile movieCoverFile);

    ResponseVo update(BetMovie betMovie, MultipartFile movieCoverFile);

    ResponseVo onShelf(int id);

    ResponseVo shelf(int id);
}
