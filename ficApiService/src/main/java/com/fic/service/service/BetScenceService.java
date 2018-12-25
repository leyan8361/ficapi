package com.fic.service.service;

import com.fic.service.Vo.BetScenceVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetScence;
import com.fic.service.entity.BetScenceMovie;

import java.math.BigDecimal;

/**
 * @Author Xie
 * @Date 2018/12/17
 * @Discription:
 **/
public interface BetScenceService {

    ResponseVo getScence(int betType,int userId);

    ResponseVo getScenceMovie(int scenceMovieId);

    ResponseVo add(BetScenceVo betScenceVo);

    ResponseVo update(BetScenceVo betScenceVo);

    ResponseVo bet(int userId, int scenceMovieId, BigDecimal amount,String betWhich);

    ResponseVo getMyBetRecord(int userId);

    ResponseVo onShelf(int id);

    ResponseVo shelf(int id);
}
