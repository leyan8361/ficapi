package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.BootPageVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.controller.BootPageController;
import com.fic.service.entity.BootPage;
import com.fic.service.mapper.BootPageMapper;
import com.fic.service.service.BootPageService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BootPageServiceImpl implements BootPageService {

    private final Logger log = LoggerFactory.getLogger(BootPageServiceImpl.class);

    @Autowired
    BootPageMapper bootPageMapper;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    UploadProperties uploadProperties;

    @Override
    public ResponseVo getAll() {
        List<BootPage> result = bootPageMapper.findAll();
        for(BootPage bootPage : result){
                bootPage.setBootPageUrl(uploadProperties.getUrl(bootPage.getBootPageUrl()));
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(BootPage bootPage, MultipartFile pageCoverFile) {

        if(null == pageCoverFile || pageCoverFile.isEmpty()){
            log.error(" add boot page failed .page cover file is null");
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }

        int saveResult = bootPageMapper.insertSelective(bootPage);
        if(saveResult <=0){
            log.error(" add boot page failed .");
            throw new RuntimeException();
        }

        String fileType = "";
        String fileName = pageCoverFile.getOriginalFilename();
        if(!RegexUtil.isPic(fileName)){
            return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
        }
        fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String newName = DateUtil.getTimeStamp()+"."+fileType;
        String newDir = Constants.BOOT_PAGE_COVER+bootPage.getId()+"/";
        String newPath = newDir+newName;
        ErrorCodeEnum saveCode = fileUtil.saveFile(pageCoverFile,newDir,newName);
        if(!saveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(saveCode,null);

        int updatePageUrl = bootPageMapper.updatePageCoverUrl(bootPage.getId(),newPath);
        if(updatePageUrl <=0){
            log.error(" add boot page failed , id:{}",bootPage.getId());
            throw new RuntimeException();
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo update(BootPage bootPage, MultipartFile pageCoverFile) {

        if(null == bootPage || null == bootPage.getId()){
            return new ResponseVo(ErrorCodeEnum.PARAMETER_MISSED,null);
        }

        int saveResult = bootPageMapper.updateByPrimaryKeySelective(bootPage);
        if(saveResult <=0){
            log.error(" update boot page failed .");
            throw new RuntimeException();
        }

        if(null != pageCoverFile && !pageCoverFile.isEmpty()){
            String fileType = "";
            String fileName = pageCoverFile.getOriginalFilename();
            if(!RegexUtil.isPic(fileName)){
                return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
            }
            fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String newName = DateUtil.getTimeStamp()+"."+fileType;
            String newDir = Constants.BOOT_PAGE_COVER+bootPage.getId()+"/";
            String newPath = newDir+newName;
            ErrorCodeEnum saveCode = fileUtil.saveFile(pageCoverFile,newDir,newName);
            if(!saveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(saveCode,null);

            int updatePageUrl = bootPageMapper.updatePageCoverUrl(bootPage.getId(),newPath);
            if(updatePageUrl <=0){
                log.error(" add boot page failed , id:{}",bootPage.getId());
                throw new RuntimeException();
            }
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo delete(int id) {
        int deleteResult = bootPageMapper.deleteByPrimaryKey(id);
        if(deleteResult <=0){
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo getPages() {
        List<BootPageVo> resultList = new ArrayList<>();
        List<BootPage> findResult = bootPageMapper.findAll();
        for(BootPage bootPage : findResult){
            if(bootPage.getIsShow() == 1){
                BootPageVo result = new BootPageVo();
                result.setId(bootPage.getId());
                result.setBootPageUrl(uploadProperties.getUrl(bootPage.getBootPageUrl()));
                resultList.add(result);
            }
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,resultList);
    }
}
