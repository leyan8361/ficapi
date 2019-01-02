package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.ShelfStatusEnum;
import com.fic.service.Vo.MovieDetailInfoVo;
import com.fic.service.Vo.MovieInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.*;
import com.fic.service.mapper.*;
import com.fic.service.service.MovieService;
import com.fic.service.utils.BeanUtil;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.RegexUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    ActorInfoMapper actorInfoMapper;
    @Autowired
    MovieDetailInfoMapper movieDetailInfoMapper;


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
    public List<MovieInfoVo> getAll() {
        List<Movie> movieList = movieMapper.findAll();
        List<MovieInfoVo> resultList = new ArrayList<MovieInfoVo>();
        for(Movie movie: movieList){
            MovieInfoVo result = new MovieInfoVo();


            List<ActorInfo> actorInfos = actorInfoMapper.findAllByMovieId(movie.getMovieId());
            if(actorInfos.size() >0){
                for(ActorInfo actorInfo : actorInfos){
                    actorInfo.setRoleCoverUrl(uploadProperties.getUrl(actorInfo.getRoleCoverUrl()));
                }
                movie.setActorArray(actorInfos);
            }

            MovieDetailInfo movieDetailInfo = movieDetailInfoMapper.findByMovieId(movie.getMovieId());
            if(null != movieDetailInfo){
                if(StringUtils.isNotEmpty(movieDetailInfo.getBriefUrl())){
                    movieDetailInfo.setBriefUrl(uploadProperties.getUrl(movieDetailInfo.getBriefUrl()));
                }
                if(StringUtils.isNotEmpty(movieDetailInfo.getPlotSummaryUrl())){
                    movieDetailInfo.setPlotSummaryUrl(uploadProperties.getUrl(movieDetailInfo.getPlotSummaryUrl()));
                }
                movie.setMovieDetailInfo(movieDetailInfo);
            }
            movie.setMovieCoverUrl(uploadProperties.getUrl(movie.getMovieCoverUrl()));

            movie.setInvestCount(investDetailMapper.countInvestPeople(movie.getMovieId()).size());
            BigDecimal totalAmount = investDetailMapper.sumTotalInvestByMovieId(movie.getMovieId());
            result.setInvestTotalAmount(null != totalAmount ? totalAmount : BigDecimal.ZERO);

            if(StringUtils.isNotEmpty(movie.getDutyDescription())){
                String [] dutyArray = movie.getDutyDescription().split("、");
                if(dutyArray.length >0){
                    result.setDutyDescriptionArray(dutyArray);
                }
            }
            BeanUtil.copy(result,movie);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(Movie movie, MultipartFile movieCoverFile) {
        if(null == movie || null == movieCoverFile || movieCoverFile.isEmpty()){
            log.error(" 新增电影为空 ， 或电影封面为空");
            throw new RuntimeException();
        }

        int checkIfExistByName = movieMapper.checkIfExistByName(movie.getMovieName());
        if(checkIfExistByName>0){
            log.debug("电影已存在 movie name :{}",movie.getMovieName());
            return new ResponseVo(ErrorCodeEnum.MOVIE_EXIST,null);
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
        BeanUtil.copy(existMovie,movie);

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
    public ResponseVo addActorInfo(int movieId,String role,String roleName,MultipartFile movieCoverFile) {

        if(null == movieCoverFile || movieCoverFile.isEmpty()){
            log.error(" 演员列表封面为空 ");
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }

        Movie movie = movieMapper.selectByPrimaryKey(movieId);
        if(null == movie){
            log.error("查找电影失败 id:{}",movieId);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        ActorInfo actorInfo = new ActorInfo();
        actorInfo.setRole(role);
        actorInfo.setRoleName(roleName);
        actorInfo.setMovieId(movieId);
        int saveResult = actorInfoMapper.insertSelective(actorInfo);
        if(saveResult <=0){
            log.error("add actor info failed . movieId :{}",movieId);
            throw new RuntimeException();
        }

        String fileType = "";
        String fileName = movieCoverFile.getOriginalFilename();
        if(!RegexUtil.isPic(fileName)){
            log.error("add actor info , file name :{}",fileName);
            return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
        }
        fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String newName = DateUtil.getTimeStamp()+"."+fileType;
        String newDir = Constants.MEDIA_PREFIX_ACTOR+movieId+"/"+actorInfo.getId()+"/";
        String newPath = newDir+newName;
        ErrorCodeEnum saveCode = fileUtil.saveFile(movieCoverFile,newDir,newName);
        if(!saveCode.equals(ErrorCodeEnum.SUCCESS)){
            log.error(" add app version file  失败 保存文件");
            throw new RuntimeException();
        }
        int updateCoverUrl = actorInfoMapper.updateCoverUrl(actorInfo.getId(),newPath);

        if(updateCoverUrl <=0){
            log.error(" add actor info update cover url failed actor info ID:{} ",actorInfo.getId());
            throw new RuntimeException();
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo updateActorInfo(int actorId, String role, String roleName, MultipartFile movieCoverFile) {

        ActorInfo actorInfo = actorInfoMapper.selectByPrimaryKey(actorId);
        if(null == actorInfo){
            log.error(" update actor info find actor info failed ,id:{}",actorId);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }

        actorInfo.setRole(role);
        actorInfo.setId(actorId);
        actorInfo.setRoleName(roleName);

        int saveResult = actorInfoMapper.updateByPrimaryKeySelective(actorInfo);
        if(saveResult <=0){
            log.error("update actor info failed . movieId :{}",actorInfo.getId());
            throw new RuntimeException();
        }
        if(null != movieCoverFile && !movieCoverFile.isEmpty()){
            String fileType = "";
            String fileName = movieCoverFile.getOriginalFilename();
            if(!RegexUtil.isPic(fileName)){
                log.error("update actor info , file name :{}",fileName);
                return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
            }
            fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String newName = DateUtil.getTimeStamp()+"."+fileType;
            String newDir = Constants.MEDIA_PREFIX_ACTOR+actorInfo.getMovieId()+"/"+actorInfo.getId()+"/";
            String newPath = newDir+newName;
            ErrorCodeEnum saveCode = fileUtil.saveFile(movieCoverFile,newDir,newName);
            if(!saveCode.equals(ErrorCodeEnum.SUCCESS)){
                log.error(" update app version file  失败 保存文件");
                throw new RuntimeException();
            }

            int updateCoverUrl = actorInfoMapper.updateCoverUrl(actorInfo.getId(),newPath);
            if(updateCoverUrl <=0){
                log.error(" update actor info update cover url failed actor info ID:{} ",actorInfo.getId());
                throw new RuntimeException();
            }
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo addBrief(int movieId, String brief, String plotSummary, MultipartFile briefCoverFile, MultipartFile plotSummaryCoverFile) {
        if(null == plotSummaryCoverFile || plotSummaryCoverFile.isEmpty() || null == briefCoverFile || briefCoverFile.isEmpty()){
            log.error(" add brief failed , briefCoverFile null or plotSummaryCoverFile null");
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        Movie movie = movieMapper.selectByPrimaryKey(movieId);
        if(null == movie){
            log.error("查找电影失败 id:{}",movieId);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        MovieDetailInfo movieDetailInfo = new MovieDetailInfo();
        movieDetailInfo.setBriefText(brief);
        movieDetailInfo.setMovieId(movieId);
        movieDetailInfo.setPlotSummary(plotSummary);
        int saveResult = movieDetailInfoMapper.insertSelective(movieDetailInfo);
        if(saveResult <=0){
            log.error(" save movie detail info failed . movie id :{}",movieId);
            throw new RuntimeException();
        }

        String briefFileType = "";
        String briefFileName = briefCoverFile.getOriginalFilename();
        if(!RegexUtil.isPic(briefFileName)){
            log.error("save movie detail info, file name :{}",briefFileName);
            return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
        }
        briefFileType = briefFileName.substring(briefFileName.lastIndexOf(".")+1,briefFileName.length());
        String briefNewName = DateUtil.getTimeStamp()+"."+briefFileType;
        String briefNewDir = Constants.MEDIA_PREFIX_BRIEF+movieId+"/"+movieDetailInfo.getId()+"/";
        String briefNewPath = briefNewDir+briefNewName;
        ErrorCodeEnum briefSaveCode = fileUtil.saveFile(briefCoverFile,briefNewDir,briefNewName);
        if(!briefSaveCode.equals(ErrorCodeEnum.SUCCESS)){
            log.error(" save movie detail info briefFile  失败 保存文件");
            throw new RuntimeException();
        }

        int updateBriefCoverResult = movieDetailInfoMapper.updateBriefCoverUrl(movieId,briefNewPath);
        if(updateBriefCoverResult <=0){
            log.error("update movie detail info briefcover url failed. movie detail info id :{}",movieDetailInfo.getId());
            throw new RuntimeException();
        }

        String plotFileType = "";
        String plotFileName = plotSummaryCoverFile.getOriginalFilename();
        if(!RegexUtil.isPic(plotFileName)){
            log.error("save movie detail info, file name :{}",plotFileName);
            return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
        }
        plotFileType = plotFileName.substring(plotFileName.lastIndexOf(".")+1,plotFileName.length());
        String plotNewName = DateUtil.getTimeStamp()+"."+plotFileType;
        String plotNewDir = Constants.MEDIA_PREFIX_PLOT+movieId+"/"+movieDetailInfo.getId()+"/";
        String plotNewPath = plotNewDir+plotNewName;
        ErrorCodeEnum plotSaveCode = fileUtil.saveFile(plotSummaryCoverFile,plotNewDir,plotNewName);
        if(!plotSaveCode.equals(ErrorCodeEnum.SUCCESS)){
            log.error(" save movie detail info plotSummaryCoverFile  失败 保存文件");
            throw new RuntimeException();
        }
        int updatePlotCoverResult = movieDetailInfoMapper.updatePlotCoverUrl(movieId,plotNewPath);
        if(updatePlotCoverResult <=0){
            log.error("update movie detail info plotSummaryCover url failed. movie detail info id :{}",movieDetailInfo.getId());
            throw new RuntimeException();
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo updateBrief(int briefId, String brief, String plotSummary, MultipartFile briefCoverFile, MultipartFile plotSummaryCoverFile) {

        MovieDetailInfo movieDetailInfo = movieDetailInfoMapper.selectByPrimaryKey(briefId);
        if(null == movieDetailInfo){
            log.error(" update brief movie detail info not found id:{}",briefId);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        if(StringUtils.isNotEmpty(brief)){
            movieDetailInfo.setBriefText(brief);
        }
        if(StringUtils.isNotEmpty(plotSummary)){
            movieDetailInfo.setPlotSummary(plotSummary);
        }
        movieDetailInfo.setId(briefId);


        if(null != briefCoverFile && !briefCoverFile.isEmpty()){
            String briefFileType = "";
            String briefFileName = briefCoverFile.getOriginalFilename();
            if(!RegexUtil.isPic(briefFileName)){
                log.error("update movie detail info, file name :{}",briefFileName);
                return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
            }
            briefFileType = briefFileName.substring(briefFileName.lastIndexOf(".")+1,briefFileName.length());
            String briefNewName = DateUtil.getTimeStamp()+"."+briefFileType;
            String briefNewDir = Constants.MEDIA_PREFIX_BRIEF+movieDetailInfo.getMovieId()+"/"+movieDetailInfo.getId()+"/";
            String briefNewPath = briefNewDir+briefNewName;
            ErrorCodeEnum briefSaveCode = fileUtil.saveFile(briefCoverFile,briefNewDir,briefNewName);
            if(!briefSaveCode.equals(ErrorCodeEnum.SUCCESS)){
                log.error(" update movie detail info briefFile  失败 保存文件");
                throw new RuntimeException();
            }
            movieDetailInfo.setBriefUrl(briefNewPath);
        }

        if(null != plotSummaryCoverFile && !plotSummaryCoverFile.isEmpty()){
            String plotFileType = "";
            String plotFileName = plotSummaryCoverFile.getOriginalFilename();
            if(!RegexUtil.isPic(plotFileName)){
                log.error("update movie detail info, file name :{}",plotFileName);
                return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
            }
            plotFileType = plotFileName.substring(plotFileName.lastIndexOf(".")+1,plotFileName.length());
            String plotNewName = DateUtil.getTimeStamp()+"."+plotFileType;
            String plotNewDir = Constants.MEDIA_PREFIX_PLOT+movieDetailInfo.getMovieId()+"/"+movieDetailInfo.getId()+"/";
            String plotNewPath = plotNewDir+plotNewName;
            ErrorCodeEnum plotSaveCode = fileUtil.saveFile(plotSummaryCoverFile,plotNewDir,plotNewName);
            if(!plotSaveCode.equals(ErrorCodeEnum.SUCCESS)){
                log.error(" update movie detail info plotSummaryCoverFile  失败 保存文件");
                throw new RuntimeException();
            }
            movieDetailInfo.setPlotSummaryUrl(plotNewPath);
        }

        int saveResult = movieDetailInfoMapper.updateByPrimaryKeySelective(movieDetailInfo);
        if(saveResult <=0){
            log.error(" update movie detail info failed . movie id :{}",movieDetailInfo.getMovieId());
            throw new RuntimeException();
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
