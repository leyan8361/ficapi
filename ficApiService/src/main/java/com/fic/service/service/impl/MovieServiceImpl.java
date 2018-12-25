package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.MovieEnum;
import com.fic.service.Vo.MovieDetailInfoVo;
import com.fic.service.Vo.MovieInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.Movie;
import com.fic.service.entity.MovieMedia;
import com.fic.service.entity.MovieUserInfo;
import com.fic.service.entity.User;
import com.fic.service.mapper.*;
import com.fic.service.service.MovieService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.RegexUtil;
import okhttp3.internal.http2.ErrorCode;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final Logger log = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Autowired
    MovieMapper movieMapper;
    @Autowired
    MovieMediaMapper movieMediaMapper;
    @Autowired
    InvestDetailMapper investDetailMapper;
    @Autowired
    MovieUserInfoMapper movieUserInfoMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UploadProperties uploadProperties;
    @Autowired
    FileUtil fileUtil;

    @Override
    public ResponseVo getMovies() {
        List<MovieInfoVo> resultList = new ArrayList<MovieInfoVo>();

        List<Movie> movieList = movieMapper.findAll();
        if(movieList.size() <=0 )return new ResponseVo(ErrorCodeEnum.MOVIE_NOT_FOUND,null);
        for(Movie movie: movieList){
            MovieInfoVo result = new MovieInfoVo();
            MovieMedia media = movieMediaMapper.findByMovieId(movie.getMovieId());
            if(null != media){
                result.setMovieCoverUrl(uploadProperties.getUrl(media.getImageUrl()));
            }
            result.setMovieId(movie.getMovieId());
            result.setMovieName(movie.getMovieName());
            result.setMovieRemark(movie.getMovieRemark());

            result.setInvestCount(investDetailMapper.countInvestPeople(movie.getMovieId()).size());
            BigDecimal totalAmount = investDetailMapper.sumTotalInvestByMovieId(movie.getMovieId());
            result.setInvestTotalAmount(null != totalAmount ? totalAmount : BigDecimal.ZERO);
            resultList.add(result);
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }

    @Override
    public ResponseVo getMovieInfo(Integer userId,Integer movieId) {
        Integer existUserId = userMapper.checkIfExistByUserId(userId);
        if(null == existUserId || existUserId == 0){
            log.error("用户不存在，Like Movie User ID : {}",userId);
            throw new RuntimeException();
        }
        Integer existMovieid = movieMapper.checkIfExistById(movieId);
        if(null == existMovieid || existMovieid ==0 ){
            log.error("电影不存在，like Movie Movie ID : {}", movieId);
            throw new RuntimeException();
        }
        MovieUserInfo movieUserInfo = movieUserInfoMapper.findByUserIdAndMovieId(userId,movieId);
        MovieDetailInfoVo detailInfoVo = new MovieDetailInfoVo();
        detailInfoVo.setMovieId(movieId);
        if(null != movieUserInfo){
            detailInfoVo.setFav(movieUserInfo.getFav());
            detailInfoVo.setLikz(movieUserInfo.getLikz());
        }
        detailInfoVo.setCountAlike(movieUserInfoMapper.countAlike(movieId));
        detailInfoVo.setCountCollect(movieUserInfoMapper.countCollect(movieId));
        return new ResponseVo(ErrorCodeEnum.SUCCESS,detailInfoVo);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo doLikeMovie(Integer userId, Integer movieId) {
        Integer existUserId = userMapper.checkIfExistByUserId(userId);
        if(null == existUserId || existUserId == 0){
            log.error("用户不存在，Like Movie User ID : {}",userId);
            throw new RuntimeException();
        }
        Integer existMovieid = movieMapper.checkIfExistById(movieId);
        if(null == existMovieid || existMovieid ==0 ){
            log.error("电影不存在，like Movie Movie ID : {}", movieId);
            throw new RuntimeException();
        }
        MovieUserInfo movieUserInfo = movieUserInfoMapper.findByUserIdAndMovieId(userId,movieId);
        if(null == movieUserInfo){
            movieUserInfo = new MovieUserInfo();
            movieUserInfo.setFav((byte)0);
            movieUserInfo.setLikz((byte)1);
            movieUserInfo.setUserId(userId);
            movieUserInfo.setMovieId(movieId);
        }else{
            if(movieUserInfo.getLikz()==(byte)0)
                movieUserInfo.setLikz((byte)1);
            else
                movieUserInfo.setLikz((byte)0);
        }
        int saveOrUpdateResult = 0;
        if(null != movieUserInfo.getId()){
            saveOrUpdateResult = movieUserInfoMapper.updateByPrimaryKey(movieUserInfo);
        }else{
            saveOrUpdateResult = movieUserInfoMapper.insertSelective(movieUserInfo);
        }
        if(saveOrUpdateResult <=0){
            log.error("更新保存 Movie Like失败 , MovieServiceImpl : 80 row , userID :{}, movieId:{}",userId,movieUserInfo);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo doFavMovie(Integer userId, Integer movieId) {
        Integer existUserId = userMapper.checkIfExistByUserId(userId);
        if(null == existUserId || existUserId == 0){
            log.error("用户不存在，Fav Movie User ID : {}",userId);
            throw new RuntimeException();
        }
        Integer existMovieId = movieMapper.checkIfExistById(movieId);
        if(null == existMovieId || existMovieId ==0 ){
            log.error("电影不存在，Fav Movie Movie ID : {}", movieId);
        }
        MovieUserInfo movieUserInfo = movieUserInfoMapper.findByUserIdAndMovieId(userId,movieId);
        if(null == movieUserInfo){
            movieUserInfo = new MovieUserInfo();
            movieUserInfo.setLikz((byte)0);
            movieUserInfo.setFav((byte)1);
            movieUserInfo.setUserId(userId);
            movieUserInfo.setMovieId(movieId);
        }else{
            if(movieUserInfo.getFav()==(byte)0)
                movieUserInfo.setFav((byte)1);
            else
                movieUserInfo.setFav((byte)0);
        }
        int saveOrUpdateResult = 0;
        if(null != movieUserInfo.getId()){
            saveOrUpdateResult = movieUserInfoMapper.updateByPrimaryKey(movieUserInfo);
        }else{
            saveOrUpdateResult = movieUserInfoMapper.insertSelective(movieUserInfo);
        }
        if(saveOrUpdateResult <=0){
            log.error("更新保存 Movie Fav失败 , MovieServiceImpl : userID :{}, movieId:{}",userId,movieUserInfo);
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public List<Movie> getAll() {
        List<Movie> movieList = movieMapper.findAll();
        return movieList;
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(Movie movie, MultipartFile movieCoverFile) {
        if(null == movie || null == movieCoverFile || movieCoverFile.isEmpty()){
            log.error(" 新增电影为空 ， 或电影封面为空");
            throw new RuntimeException();
        }

        int saveMovieResult = movieMapper.insertSelective(movie);
        if( saveMovieResult <=0){
            log.error(" 保存电影失败 {}",movie.toString());
            throw new RuntimeException();
        }

        String fileType = "";
        String fileName = movieCoverFile.getOriginalFilename();
        if(!RegexUtil.isPic(fileName)){
            log.error(" 保存电影失败 文件类型非图片 {}",movie.toString());
            throw new RuntimeException();
        }
        fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String newName = DateUtil.getTimeStamp()+"."+fileType;
        String newDir = Constants.MOVIE_COVER + movie.getMovieId()+"/";
        String newPath = newDir+newName;
        ErrorCodeEnum saveCode = fileUtil.saveFile(movieCoverFile,newDir,newName);
        if(!saveCode.equals(ErrorCodeEnum.SUCCESS)){
            log.error(" add movie 失败 保存文件");
            throw new RuntimeException();
        }

        int updateMovieCoverResult = movieMapper.updateMovieCover(newPath,movie.getMovieId());

        if( updateMovieCoverResult <=0){
            log.error(" add movie update cover failed  id : {}",movie.getMovieId());
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo update(Movie movie,MultipartFile movieCoverFile) {
        if(null == movie || null == movie.getMovieId()){
            log.error(" 更新电影失败 电影为空");
            throw new RuntimeException();
        }

        Movie existMovie = movieMapper.selectByPrimaryKey(movie.getMovieId());
        try{
            BeanUtil.copy(existMovie,movie);
        } catch (IllegalAccessException e) {
            log.error(" 更新电影失败 异常 :{}",e);
            throw new RuntimeException();
        } catch (InvocationTargetException e) {
            log.error(" 更新电影失败 异常 :{}",e);
            throw new RuntimeException();
        }

        int updateMovieResult = movieMapper.updateByPrimaryKeySelective(existMovie);

        if( updateMovieResult <=0){
            log.error(" 保存电影失败 {}",movie.toString());
            throw new RuntimeException();
        }

        if(null != movieCoverFile && !movieCoverFile.isEmpty()){
            String fileType = "";
            String fileName = movieCoverFile.getOriginalFilename();
            if(!RegexUtil.isPic(fileName)){
                log.error(" 更新电影失败 文件类型非图片 {}",movie.toString());
                throw new RuntimeException();
            }
            fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String newName = DateUtil.getTimeStamp()+"."+fileType;
            String newDir = Constants.MOVIE_COVER + existMovie.getMovieId()+"/";
            String newPath = newDir+newName;
            ErrorCodeEnum saveCode = fileUtil.saveFile(movieCoverFile,newDir,newName);
            if(!saveCode.equals(ErrorCodeEnum.SUCCESS)){
                log.error(" add movie 失败 保存文件");
                throw new RuntimeException();
            }
            int updateMovieCoverResult = movieMapper.updateMovieCover(newPath,existMovie.getMovieId());
            if( updateMovieCoverResult <=0){
                log.error(" update movie update cover failed  id : {}",existMovie.getMovieId());
                throw new RuntimeException();
            }
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo onShelf(int id) {
        int onShelfResult = movieMapper.updateStatus(MovieEnum.ON_SHELF.getCode(),id);
        if(onShelfResult <=0){
            log.error(" 上架失败 电影ID : {}",id);

        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo shelf(int id) {
        int onShelfResult = movieMapper.updateStatus(MovieEnum.SHELF.getCode(),id);
        if(onShelfResult <=0){
            log.error(" 下架失败 电影ID : {}",id);

        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
