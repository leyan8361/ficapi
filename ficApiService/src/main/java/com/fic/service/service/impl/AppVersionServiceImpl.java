package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.controller.AppVersionController;
import com.fic.service.entity.AppVersion;
import com.fic.service.mapper.AppVersionMapper;
import com.fic.service.service.AppVersionService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    private final Logger log = LoggerFactory.getLogger(AppVersionServiceImpl.class);

    @Autowired
    AppVersionMapper appVersionMapper;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    UploadProperties uploadProperties;

    @Override
    public ResponseVo getAll() {
        List<AppVersion> result = appVersionMapper.findAll();
        for(AppVersion appVersion : result){
            appVersion.setDownloadUrl(uploadProperties.getUrl(appVersion.getDownloadUrl()));
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    public ResponseVo add(String version, int deviceType, MultipartFile appFile) {
        int checkIfExist = appVersionMapper.checkByVersionInType(version,deviceType);
        if(checkIfExist >0){
            return new ResponseVo(ErrorCodeEnum.VERSION_EXIST,null);
        }

        AppVersion appVersion = new AppVersion();
        appVersion.setDeviceType(deviceType);
        appVersion.setDownloadCount(0);
        appVersion.setVersion(version);
        appVersion.setCreatedTime(new Date());
        appVersion.setUpdatedTime(new Date());
        int saveVersionResult = appVersionMapper.insertSelective(appVersion);
        if(saveVersionResult <=0){
            log.error(" add version failed .");
            throw new RuntimeException();
        }

        if(null != appFile && !appFile.isEmpty()){
            String fileType = "";
            String fileName = appFile.getOriginalFilename();
            if(!RegexUtil.isApkOrIpa(fileName)){
                log.error("add app version file is not apk or ipa, file name :{}",fileName);
                return new ResponseVo(ErrorCodeEnum.VERSION_FILE_TYPE_NOT_MATCH,null);
            }
            fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String newName = DateUtil.getTimeStamp()+"."+fileType;
            String newDir = Constants.APP_FILE_PATH + appVersion.getId()+"/";
            String newPath = newDir+newName;
            ErrorCodeEnum saveCode = fileUtil.saveFile(appFile,newDir,newName);
            if(!saveCode.equals(ErrorCodeEnum.SUCCESS)){
                log.error(" add app version file  失败 保存文件");
                throw new RuntimeException();
            }
            int updateDownloadUrlResult = appVersionMapper.updateFileUrl(newPath,appVersion.getId());
            if( updateDownloadUrlResult <=0){
                log.error(" add app version file  update download url failed  : {}",appVersion.getId());
                throw new RuntimeException();
            }
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public ResponseVo update(int id,String version, Integer deviceType, MultipartFile appFile) {
        AppVersion appVersionExist = appVersionMapper.selectByPrimaryKey(id);
        if(null == appVersionExist){
            return new ResponseVo(ErrorCodeEnum.VERSION_NOT_EXIST,null);
        }
        if(StringUtils.isNotEmpty(version)){
            appVersionExist.setVersion(version);
        }
        if(null != deviceType) {
            appVersionExist.setDeviceType(deviceType);
        }
        if(null != appFile && !appFile.isEmpty()){
            String fileType = "";
            String fileName = appFile.getOriginalFilename();
            if(!RegexUtil.isApkOrIpa(fileName)){
                log.error("update app version file is not apk or ipa, file name :{}",fileName);
                throw new RuntimeException();
            }
            fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String newName = DateUtil.getTimeStamp()+"."+fileType;
            String newDir = Constants.APP_FILE_PATH + appVersionExist.getId()+"/";
            String newPath = newDir+newName;
            ErrorCodeEnum saveCode = fileUtil.saveFile(appFile,newDir,newName);
            if(!saveCode.equals(ErrorCodeEnum.SUCCESS)){
                log.error(" update app version file  失败 保存文件");
                throw new RuntimeException();
            }
            appVersionExist.setDownloadUrl(newPath);
        }

        int updateResult = appVersionMapper.updateByPrimaryKeySelective(appVersionExist);
        if( updateResult <=0){
            log.error(" update app version file  update download url failed  : {}",appVersionExist.getId());
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
