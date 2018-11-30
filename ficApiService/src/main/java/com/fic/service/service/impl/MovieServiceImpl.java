package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.MovieInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.Movie;
import com.fic.service.entity.MovieMedia;
import com.fic.service.mapper.InvestDetailMapper;
import com.fic.service.mapper.MovieMapper;
import com.fic.service.mapper.MovieMediaMapper;
import com.fic.service.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ResponseVo getMovieInfo() {
        List<MovieInfoVo> resultList = new ArrayList<MovieInfoVo>();

        List<Movie> movieList = movieMapper.findAll();
        if(movieList.size() <=0 )return new ResponseVo(ErrorCodeEnum.MOVIE_NOT_FOUND,null);
        for(Movie movie: movieList){
            MovieInfoVo result = new MovieInfoVo();
            MovieMedia media = movieMediaMapper.findByMovieId(movie.getMovieId());
            if(null != media){
                result.setMovieCoverUrl(media.getImageUrl());
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
}
