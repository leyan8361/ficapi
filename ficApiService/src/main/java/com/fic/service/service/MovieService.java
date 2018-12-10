package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;

public interface MovieService {

    ResponseVo getMovieInfo();

    ResponseVo doLikeMovie(Integer userId,Integer movieId);

    ResponseVo doFavMovie(Integer userId,Integer movieId);
}
