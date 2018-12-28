package com.fic.service.Vo;

import com.fic.service.entity.Movie;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class MoiveAddInfoVo extends Movie {

    @ApiModelProperty("电影封面File")
    private MultipartFile movieCoverFile;

    public MultipartFile getMovieCoverFile() {
        return movieCoverFile;
    }

    public void setMovieCoverFile(MultipartFile movieCoverFile) {
        this.movieCoverFile = movieCoverFile;
    }
}
