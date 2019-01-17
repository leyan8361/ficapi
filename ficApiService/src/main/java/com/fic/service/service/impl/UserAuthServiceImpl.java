package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.UserAuthStatusEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.controller.api.v1.ApiUserAuthController;
import com.fic.service.entity.User;
import com.fic.service.entity.UserAuth;
import com.fic.service.mapper.UserAuthMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.UserAuthService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.RegexUtil;
import jnr.x86asm.ERROR_CODE;
import okhttp3.internal.http2.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final Logger log = LoggerFactory.getLogger(UserAuthServiceImpl.class);

    @Autowired
    UserAuthMapper userAuthMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    UploadProperties uploadProperties;

    @Override
    public List<UserAuth> findAllByStatus(int type) {
        List<UserAuth> userAuths = userAuthMapper.findAllByStatus(type);
        for(UserAuth userAuth : userAuths){
            userAuth.setFrontFaceUrl(uploadProperties.getUrl(userAuth.getFrontFaceUrl()));
            userAuth.setBackFaceUrl(uploadProperties.getUrl(userAuth.getBackFaceUrl()));
        }
        return userAuths;
    }

    @Override
    public ResponseVo findByUserId(int userId) {
        UserAuth result = userAuthMapper.findByUserId(userId);
        if(null == result){
            return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
        }
        result.setFrontFaceUrl(uploadProperties.getUrl(result.getFrontFaceUrl()));
        result.setBackFaceUrl(uploadProperties.getUrl(result.getBackFaceUrl()));
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(int userId, String cerId, String name, MultipartFile frontFaceFile, MultipartFile backFaceFile) {

        int countExist = userAuthMapper.checkIfExist(userId);
        if(countExist >0){
            log.error("用户实名认证数据已存在 userId :{}",userId);
            return new ResponseVo(ErrorCodeEnum.USER_ALREADY_AUTH,null);
        }

        User user = userMapper.get(userId);
        if(null == user){
            return new ResponseVo(ErrorCodeEnum.USER_NOT_EXIST,null);
        }

        if(null == frontFaceFile || frontFaceFile.isEmpty()){
            log.debug("实名认证，缺少正面");
            return new ResponseVo(ErrorCodeEnum.USER_AUTH_FRONT_FACE_MISSED,null);
        }
        if(null == backFaceFile || backFaceFile.isEmpty()){
            log.debug("实名认证，缺少反面");
            return new ResponseVo(ErrorCodeEnum.USER_AUTH_BACK_FACE_MISSED,null);
        }

        String frontFileType = "";
        String frontFileName = frontFaceFile.getOriginalFilename();
        if(!RegexUtil.isPic(frontFileName)){
            return new ResponseVo(ErrorCodeEnum.PIC_ERROR,null);
        }
        frontFileType = frontFileName.substring(frontFileName.lastIndexOf(".")+1,frontFileName.length());
        String frontNewName = DateUtil.getTimeStamp()+"."+frontFileType;
        String frontNewDir = Constants.USER_AUTH_PATH+userId+"/"+Constants.USER_AUTH_FRONT_PATH;
        String frontNewPath = frontNewDir+frontNewName;
        ErrorCodeEnum frontSaveCode = fileUtil.saveFile(frontFaceFile,frontNewDir,frontNewName);
        if(!frontSaveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(frontSaveCode,null);

        String backFileType = "";
        String backFileName = backFaceFile.getOriginalFilename();
        if(!RegexUtil.isPic(backFileName)){
            return new ResponseVo(ErrorCodeEnum.PIC_ERROR,null);
        }
        backFileType = backFileName.substring(backFileName.lastIndexOf(".")+1,backFileName.length());
        String backNewName = DateUtil.getTimeStamp()+"."+backFileType;
        String backNewDir = Constants.USER_AUTH_PATH+userId+"/"+Constants.USER_AUTH_BACK_PATH;
        String backNewPath = backNewDir+backNewName;
        ErrorCodeEnum backSaveCode = fileUtil.saveFile(backFaceFile,backNewDir,backNewName);
        if(!backSaveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(backSaveCode,null);

        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(userId);
        userAuth.setCerId(cerId);
        userAuth.setName(name);
        userAuth.setFrontFaceUrl(frontNewPath);
        userAuth.setBackFaceUrl(backNewPath);
        userAuth.setStatus(UserAuthStatusEnum.WAIT_APPROVE.code());
        userAuth.setCreatedTime(new Date());

        int saveResult = userAuthMapper.insertSelective(userAuth);
        if(saveResult <=0){
            log.error("实名认证，保存失败 user id :{}",userId);
            throw new RuntimeException();
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo update(int userAuthId,String cerId, String name, MultipartFile frontFaceFile, MultipartFile backFaceFile) {

        UserAuth userAuth = userAuthMapper.get(userAuthId);
        if(null == userAuth){
            log.error(" 更新实名认证失败，无此认证数据 user auth id :{}",userAuthId);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }

        if(userAuth.getStatus() == UserAuthStatusEnum.APPROVE.code()){
            return new ResponseVo(ErrorCodeEnum.USER_AUTH_ALREADY_APPROVED,null);
        }

        if(null != frontFaceFile && !frontFaceFile.isEmpty()){
            String frontFileType = "";
            String frontFileName = frontFaceFile.getOriginalFilename();
            if(!RegexUtil.isPic(frontFileName)){
                return new ResponseVo(ErrorCodeEnum.PIC_ERROR,null);
            }
            frontFileType = frontFileName.substring(frontFileName.lastIndexOf(".")+1,frontFileName.length());
            String frontNewName = DateUtil.getTimeStamp()+"."+frontFileType;
            String frontNewDir = Constants.USER_AUTH_PATH+userAuth.getUserId()+"/"+Constants.USER_AUTH_FRONT_PATH;
            String frontNewPath = frontNewDir+frontNewName;
            ErrorCodeEnum frontSaveCode = fileUtil.saveFile(frontFaceFile,frontNewDir,frontNewName);
            if(!frontSaveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(frontSaveCode,null);
            userAuth.setFrontFaceUrl(frontNewPath);
        }
        if(null != backFaceFile && !backFaceFile.isEmpty()){
            String backFileType = "";
            String backFileName = backFaceFile.getOriginalFilename();
            if(!RegexUtil.isPic(backFileName)){
                return new ResponseVo(ErrorCodeEnum.PIC_ERROR,null);
            }
            backFileType = backFileName.substring(backFileName.lastIndexOf(".")+1,backFileName.length());
            String backNewName = DateUtil.getTimeStamp()+"."+backFileType;
            String backNewDir = Constants.USER_AUTH_PATH+userAuth.getUserId()+"/"+Constants.USER_AUTH_BACK_PATH;
            String backNewPath = backNewDir+backNewName;
            ErrorCodeEnum backSaveCode = fileUtil.saveFile(backFaceFile,backNewDir,backNewName);
            if(!backSaveCode.equals(ErrorCodeEnum.SUCCESS))return new ResponseVo(backSaveCode,null);
            userAuth.setBackFaceUrl(backNewPath);
        }
        if(StringUtils.isNotEmpty(cerId)){
            userAuth.setCerId(cerId);
        }
        if(StringUtils.isNotEmpty(name)){
            userAuth.setName(name);
        }
        int saveResult = userAuthMapper.updateByPrimaryKey(userAuth);
        if(saveResult <=0){
            log.error("更新实名认证，保存失败 user auth id :{}",userAuth.getId());
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo auditing(int userAuthId,boolean pass,String remark) {
        UserAuth userAuth = userAuthMapper.get(userAuthId);
        if(null == userAuth){
            log.error("审核，实名认证数据not found userAuthId :{}",userAuthId);
            return new ResponseVo(ErrorCodeEnum.USER_NOT_AUTH,null);
        }
        if(pass){
            /** 审核通过 */
            userAuth.setStatus(UserAuthStatusEnum.APPROVE.code());
        }else{
            /** 审核拒绝 */
            userAuth.setStatus(UserAuthStatusEnum.REJECT.code());
            userAuth.setRemark(remark);
        }
        int updateResult = userAuthMapper.updateByPrimaryKeySelective(userAuth);
        if(updateResult <=0){
            log.error("审核实名认证失败，更新异常");
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }
}
