package com.fic.service.service;


import com.fic.service.Vo.MovieInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.ActorInfo;
import com.fic.service.entity.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieService {

    ResponseVo getMovies();

    ResponseVo getMoviesV2();

    ResponseVo getMovieInfo(Integer userId,Integer movieId);

    ResponseVo getMovieInfoV2(Integer userId,Integer movieId);

    ResponseVo doLikeMovie(Integer userId,Integer movieId);

    ResponseVo doFavMovie(Integer userId,Integer movieId);

    ResponseVo getAll();

    ResponseVo add(Movie movie,MultipartFile movieCoverFile);

    ResponseVo update(Movie movie,MultipartFile movieCoverFile);

    ResponseVo addActorInfo(int movieId,String role,String roleName,MultipartFile movieCoverFile);

    ResponseVo updateActorInfo(int actorId,String role,String roleName,MultipartFile movieCoverFile);

    ResponseVo addBrief(int movieId,String brief,String plotSummary,MultipartFile briefCoverFile,MultipartFile plotSummaryCoverFile);

    ResponseVo updateBrief(int briefId,String brief,String plotSummary,MultipartFile briefCoverFile,MultipartFile plotSummaryCoverFile);

}
