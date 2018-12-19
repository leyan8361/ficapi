package com.fic.service.service.impl;

import com.fic.service.Enum.BetScenceMovieStatusEnum;
import com.fic.service.Enum.BingoStatusEnum;
import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.controller.BetScenceMovieController;
import com.fic.service.entity.BetMovie;
import com.fic.service.entity.BetScence;
import com.fic.service.entity.BetScenceMovie;
import com.fic.service.mapper.BetMovieMapper;
import com.fic.service.mapper.BetScenceMapper;
import com.fic.service.mapper.BetScenceMovieMapper;
import com.fic.service.mapper.BetUserMapper;
import com.fic.service.service.BetScenceMovieService;
import com.fic.service.service.BetScenceService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@Service
public class BetScenceMovieServiceImpl implements BetScenceMovieService {

    private final Logger log = LoggerFactory.getLogger(BetScenceMovieServiceImpl.class);

    @Autowired
    BetScenceMovieMapper betScenceMovieMapper;
    @Autowired
    BetScenceMapper betScenceMapper;
    @Autowired
    BetMovieMapper betMovieMapper;
    @Autowired
    BetUserMapper betUserMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(BetScenceMovie scenceMovie) {
        BetScence scence = betScenceMapper.selectByPrimaryKey(scenceMovie.getBetScenceId());
        if(null == scence){
            return new ResponseVo(ErrorCodeEnum.NO_AVALIBLE_SCENCE,null);
        }
        BetMovie movie = betMovieMapper.getById(scenceMovie.getBetMovieId());
        if(null == movie){
            return new ResponseVo(ErrorCodeEnum.BET_ADD_MOVIE_NOT_FOUND,null);
        }
        scenceMovie.setStatus(BetScenceMovieStatusEnum.WAIT.getCode().byteValue());
        int saveResult = betScenceMovieMapper.insertSelective(scenceMovie);
        if(saveResult <=0){
            log.error(" 为scence 新增movie 失败 ");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }


    @Override
    public ResponseVo update(BetScenceMovie scenceMovie) {
        if(null == scenceMovie || null == scenceMovie.getId()){
            return new ResponseVo(ErrorCodeEnum.NO_AVALIBLE_SCENCE,null);
        }
        BetScenceMovie scenceMovieExist = betScenceMovieMapper.selectByPrimaryKey(scenceMovie.getId());
        BeanUtil.copy(scenceMovieExist,scenceMovie);
        int updateResult = betScenceMovieMapper.updateByPrimaryKeySelective(scenceMovie);
        if(updateResult <=0){
            log.error(" 为scence 更新movie 失败 ");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo delete(int id) {
        int checkAlreadyBetUser = betUserMapper.checkAlreadyBetUser(id);
        if(checkAlreadyBetUser > 0){
            return new ResponseVo(ErrorCodeEnum.THE_SCENCE_MOVIE_ALREAY_BE_BET,null);
        }
        int deleteResult = betScenceMovieMapper.deleteByPrimaryKey(id);
        if(deleteResult <=0){
            log.error(" 删除bet_scence_movie失败 id :{} ",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
