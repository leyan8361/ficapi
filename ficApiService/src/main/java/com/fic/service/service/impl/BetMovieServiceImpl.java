package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.ShelfStatusEnum;
import com.fic.service.Vo.BetMovieVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.BetMovie;
import com.fic.service.entity.BetScence;
import com.fic.service.entity.BoxOffice;
import com.fic.service.entity.User;
import com.fic.service.mapper.BetMovieMapper;
import com.fic.service.mapper.BoxOfficeMapper;
import com.fic.service.service.BetMovieService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@Service
public class BetMovieServiceImpl implements BetMovieService {

    private final Logger log = LoggerFactory.getLogger(BetMovieServiceImpl.class);

    @Autowired
    BetMovieMapper betMovieMapper;
    @Autowired
    BoxOfficeMapper boxOfficeMapper;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    UploadProperties uploadProperties;

    @Override
    public ResponseVo getAll() {
        List<BetMovieVo> resultList = new ArrayList<>();
        List<BetMovie> findResult = betMovieMapper.findAll();
        if(findResult.size() ==0){
            return new ResponseVo(ErrorCodeEnum.BET_NO_MOVIE,null);
        }
        for(BetMovie betMovie: findResult){
                BetMovieVo result = new BetMovieVo();
                BeanUtil.copy(result,betMovie);
                result.setBetMovieCoverUrl(uploadProperties.getUrl(betMovie.getBetMovieCoverUrl()));

                BoxOffice boxOffice = boxOfficeMapper.findByDay(DateUtil.getYesterdayAndFormatDay(),betMovie.getId());

                if(null == boxOffice){
                    continue;
                }
                result.setBoxInfo(boxOffice.getBoxInfo());
                result.setSumDay(boxOffice.getSumDay());
                result.setSumBoxInfo(boxOffice.getSumBoxInfo() + boxOffice.getSumBoxInfoUnit());

                resultList.add(result);
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }

    @Override
    public ResponseVo getHistory() {
        List<BetMovieVo> resultList = new ArrayList<>();
        List<BoxOffice> findResult = boxOfficeMapper.findAllByDay(DateUtil.getYesterdayAndFormatDay());
        if(findResult.isEmpty()){
            return  new ResponseVo(ErrorCodeEnum.BET_BOX_NOT_FOUND,resultList);
        }
        for(BoxOffice boxOffice: findResult){
            BetMovie betMovie = betMovieMapper.selectByPrimaryKey(boxOffice.getMovieId());
            if(null == betMovie){
                continue;
            }
            BetMovieVo result = new BetMovieVo();
            BeanUtil.copy(result,betMovie);
            result.setBetMovieCoverUrl(uploadProperties.getUrl(betMovie.getBetMovieCoverUrl()));
            result.setBoxInfo(boxOffice.getBoxInfo());
            result.setSumBoxInfo(boxOffice.getSumBoxInfo() + boxOffice.getSumBoxInfoUnit());
            result.setSumDay(boxOffice.getSumDay());
            resultList.add(result);
        }
        return  new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }

    @Override
    public ResponseVo getById(int movieId) {
        BetMovieVo result = new BetMovieVo();
        BetMovie findReuslt = betMovieMapper.getById(movieId);
        if(null == findReuslt){
            return new ResponseVo(ErrorCodeEnum.BET_NO_MOVIE,null);
        }
        BeanUtil.copy(result,findReuslt);
        BoxOffice boxOffice = boxOfficeMapper.findByDay(DateUtil.getYesterdayAndFormatDay(),movieId);
        if(null == boxOffice){
            return new ResponseVo(ErrorCodeEnum.BET_BOX_NOT_FOUND,null);
        }
        result.setSumDay(boxOffice.getSumDay());
        result.setSumBoxInfo(boxOffice.getSumBoxInfo() + boxOffice.getSumBoxInfoUnit());
        result.setBoxInfo(boxOffice.getBoxInfo());
        result.setBetMovieCoverUrl(uploadProperties.getUrl(findReuslt.getBetMovieCoverUrl()));
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    public ResponseVo add(BetMovie betMovie, MultipartFile movieCoverFile) {
        if(null == betMovie || null == movieCoverFile || movieCoverFile.isEmpty()){
            log.error(" add bet movie  is null or movie cover file is null");
            return new ResponseVo(ErrorCodeEnum.BET_ADD_MOVIE_NOT_FOUND,null);
        }
        if(StringUtils.isNotEmpty(betMovie.getBetMovieName())){
            int existMovie = betMovieMapper.checkExistMovieName(betMovie.getBetMovieName());
            if(existMovie > 0){
                return new ResponseVo(ErrorCodeEnum.BET_MOVIE_EXIST,null);
            }
        }
        betMovie.setCreatedTime(new Date());
        betMovie.setStatus(ShelfStatusEnum.ON_SHELF.getCode().byteValue());
        int saveBetMovieResult = betMovieMapper.insertSelective(betMovie);
        if( saveBetMovieResult <=0){
            log.error(" add bet movie failed ");
            throw new RuntimeException();
        }

        String fileType = "";
        String fileName = movieCoverFile.getOriginalFilename();
        if(!RegexUtil.isPic(fileName)){
            return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
        }
        fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String newName = DateUtil.getTimeStamp()+"."+fileType;
        String newDir = Constants.BET_MOVIE_COVER_PATH+betMovie.getId()+"/";
        String newPath = newDir+newName;
        ErrorCodeEnum saveCode = fileUtil.saveFile(movieCoverFile,newDir,newName);
        if(!saveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(saveCode,null);

        int updateResult = betMovieMapper.updateMovieCoverURL(newPath,betMovie.getId());
        if(updateResult <=0)return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo update(BetMovie betMovie, MultipartFile movieCoverFile) {
        if(null == betMovie || null ==  betMovie.getId()){
            log.error(" update bet movie  is null or movie cover file is null");
            return new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null);
        }
        int result = betMovieMapper.checkExistMovieName(betMovie.getBetMovieName());
        if(result <=0){
            log.error("update bet movie failed .moive not found id :{}",betMovie.getId());
            return new ResponseVo(ErrorCodeEnum.BET_ADD_MOVIE_NOT_FOUND,null);
        }

        if(null != movieCoverFile && !movieCoverFile.isEmpty()){
            String fileType = "";
            String fileName = movieCoverFile.getOriginalFilename();
            if(!RegexUtil.isPic(fileName)){
                return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
            }
            fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String newName = DateUtil.getTimeStamp()+"."+fileType;
            String newDir = Constants.BET_MOVIE_COVER_PATH+betMovie.getId()+"/";
            String newPath = newDir+newName;
            ErrorCodeEnum saveCode = fileUtil.saveFile(movieCoverFile,newDir,newName);
            if(!saveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(saveCode,null);
            betMovie.setBetMovieCoverUrl(newPath);
        }

        int updateResult = betMovieMapper.updateByPrimaryKeySelective(betMovie);
        if(updateResult <=0) {
            log.error("update bet movie failed .");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo onShelf(int id) {
        int updateResult = betMovieMapper.updateStatus(ShelfStatusEnum.ON_SHELF.getCode(),id);
        if(updateResult <=0){
            log.error("竞猜电影bet movie 上架失败 id:{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo shelf(int id) {
        int updateResult = betMovieMapper.updateStatus(ShelfStatusEnum.SHELF.getCode(),id);
        if(updateResult <=0){
            log.error("竞猜项目bet movie 下架失败 id:{}",id);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
