package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;

public interface MovieService {

    ResponseVo getMovies();

    ResponseVo getMovieInfo(Integer userId,Integer movieId);

    ResponseVo doLikeMovie(Integer userId,Integer movieId);

    ResponseVo doFavMovie(Integer userId,Integer movieId);
}
