package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieService {

    ResponseVo getMovies();

    ResponseVo getMovieInfo(Integer userId,Integer movieId);

    ResponseVo doLikeMovie(Integer userId,Integer movieId);

    ResponseVo doFavMovie(Integer userId,Integer movieId);

    List<Movie> getAll();

    ResponseVo add(Movie movie,MultipartFile movieCoverFile);

    ResponseVo update(Movie movie,MultipartFile movieCoverFile);

    ResponseVo onShelf(int id);

    ResponseVo shelf(int id);


}
