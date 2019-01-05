package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PageVo {

    @ApiModelProperty(value = "当前页码")
    private int pageNum;

    public int getPageNum() {
        if(pageNum>0){
            return pageNum-1;
        }
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
