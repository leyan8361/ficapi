package com.fic.service.service.impl;

import com.fic.service.entity.BoxOffice;
import com.fic.service.mapper.BoxOfficeMapper;
import com.fic.service.service.BoxOfficeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BoxOfficeServiceImpl implements BoxOfficeService {

    private final Logger log = LoggerFactory.getLogger(BoxOfficeServiceImpl.class);

    @Autowired
    BoxOfficeMapper boxOfficeMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public void add(BoxOffice boxOffice) {
        int saveResult = boxOfficeMapper.insertSelective(boxOffice);
        if(saveResult <=0){
            log.error(" save box Failed, movie : {}", boxOffice.getMovieId());
            throw new RuntimeException();
        }
    }
}
