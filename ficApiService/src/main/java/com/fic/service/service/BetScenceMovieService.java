package com.fic.service.service;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetScenceMovie;

/**
 *   @Author Xie
 *   @Date 2018/12/18
 *   @Discription:
**/
public interface BetScenceMovieService {

    ResponseVo getAll();

    ResponseVo add(BetScenceMovie scenceMovie);

    ResponseVo update(BetScenceMovie scenceMovie);

    ResponseVo delete(int id);

    ResponseVo getMovieOn();
}
