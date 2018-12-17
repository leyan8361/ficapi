package com.fic.service.service;

import com.fic.service.Vo.BetScenceVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetScence;

import java.math.BigDecimal;

/**
 * @Author Xie
 * @Date 2018/12/17
 * @Discription:
 **/
public interface BetScenceService {

    ResponseVo getScence(int betType);

    ResponseVo add(BetScenceVo betScenceVo);

    ResponseVo addMovie(int id,int movieId);

    ResponseVo bet(int userId, int scenceId, int movieId, BigDecimal amount,String betWhich);

    ResponseVo onShelf(int id);

    ResponseVo shelf(int id);
}
