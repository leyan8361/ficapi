package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.BannerInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
import com.fic.service.constants.UploadProperties;
import com.fic.service.entity.BannerInfo;
import com.fic.service.mapper.BannerInfoMapper;
import com.fic.service.service.BannerInfoService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.FileUtil;
import com.fic.service.utils.RegexUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/12/14
 *   @Discription:
**/
@Service
public class BannerInfoServiceImpl implements BannerInfoService {

    private final Logger log = LoggerFactory.getLogger(InvestServiceImpl.class);

    @Autowired
    UploadProperties uploadProperties;
    @Autowired
    BannerInfoMapper bannerInfoMapper;
    @Autowired
    FileUtil fileUtil;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo add(BannerInfoVo bannerInfoVo,MultipartFile bannerFile) {
        if(null == bannerInfoVo || null == bannerFile  ||  bannerFile.isEmpty()){
            log.error(" Banner 保存失败  :  文件为空");
            throw new RuntimeException();
        }

        BannerInfo bannerInfo = new BannerInfo();
        bannerInfo.setJumpUrlAndroid(bannerInfoVo.getJumpUrlAndroid());
        bannerInfo.setJumpUrlIos(bannerInfoVo.getJumpUrlIos());
        bannerInfo.setInOrder(bannerInfoVo.getInOrder());
        bannerInfo.setStatus(bannerInfoVo.getStatus());
        bannerInfo.setCreatedTime(new Date());

        int saveResult = bannerInfoMapper.insertSelective(bannerInfo);
        if(saveResult <=0){
            log.error("保存Banner失败 ", bannerInfo.toString());
        }

        String fileType = "";
        String fileName = bannerFile.getOriginalFilename();
        if(!RegexUtil.isPic(fileName)){
            return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
        }
        fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String newName = DateUtil.getTimeStamp()+"."+fileType;
        String newDir = Constants.BANNER_URL_PATH + bannerInfo.getId()+"/";
        String newPath = newDir+newName;
        ErrorCodeEnum saveCode = fileUtil.saveFile(bannerFile,newDir,newName);
        if(!saveCode.equals(ErrorCodeEnum.SUCCESS)){
            log.error(" add banner 失败 保存文件");
            throw new RuntimeException();
        }

        int updateBannerUrl = bannerInfoMapper.updateBannerUrl(newPath,bannerInfo.getId());
        if(updateBannerUrl <=0){
            log.error(" 新增 banner  更新url  失败 : {}" + newPath);
            throw new RuntimeException();
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo delete(int id) {
        BannerInfo bannerInfo = bannerInfoMapper.selectByPrimaryKey(id);
        String path = bannerInfo.getBannerUrl();
        int deleteResult = bannerInfoMapper.deleteByPrimaryKey(id);
        if(deleteResult <=0){
            log.error(" 删除Banner失败  id: {}",id);
            throw new RuntimeException();
        }
        fileUtil.deleteByPath(path);
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ResponseVo update(BannerInfoVo bannerInfoVo,MultipartFile bannerFile) {
        if(null == bannerInfoVo || null == bannerInfoVo.getId()){
            log.error(" 无法删除 Banner ，ID 为空");
            throw new RuntimeException();
        }

        BannerInfo bannerInfo = bannerInfoMapper.selectByPrimaryKey(bannerInfoVo.getId());

        if(null == bannerInfo){
            log.error(" 无法删除 Banner ，Banner不存在 ID :{}",bannerInfoVo.getId());
            throw new RuntimeException();
        }

        bannerInfo.setStatus(bannerInfoVo.getStatus());
        bannerInfo.setInOrder(bannerInfoVo.getInOrder());
        bannerInfo.setJumpUrlIos(bannerInfoVo.getJumpUrlIos());
        bannerInfo.setJumpUrlAndroid(bannerInfoVo.getJumpUrlAndroid());
        int updateResult = bannerInfoMapper.updateByPrimaryKey(bannerInfo);
        if(updateResult <=0){
            log.error(" update banner 失败 保存bannerInfo");
            throw new RuntimeException();
        }

        if(null != bannerFile && !bannerFile.isEmpty()){
            String fileType = "";
            String fileName = bannerFile.getOriginalFilename();
            if(!RegexUtil.isPic(fileName)){
                return new ResponseVo(ErrorCodeEnum.USER_HEAD_PIC_ERROR,null);
            }
            fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String newName = DateUtil.getTimeStamp()+"."+fileType;
            String newDir = Constants.BANNER_URL_PATH +bannerInfoVo.getId()+"/";
            String newPath = newDir+newName;
            ErrorCodeEnum saveCode = fileUtil.saveFile(bannerFile,newDir,newName);
            if(!saveCode.equals(ErrorCodeEnum.SUCCESS)){
                log.error(" update banner 失败 保存文件");
                throw new RuntimeException();
            }
            bannerInfo.setBannerUrl(newPath);
            int updateBannerUrl = bannerInfoMapper.updateBannerUrl(bannerInfo.getBannerUrl(),bannerInfo.getId());
            if(updateBannerUrl <=0){
                log.error(" 新增 banner  更新url  失败 : {}" + bannerInfo.getBannerUrl());
                throw new RuntimeException();
            }
        }

        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    @Override
    public List<BannerInfoVo> getAll() {
        List<BannerInfo> bannerInfos = bannerInfoMapper.getAll();
        List<BannerInfoVo> resultList = new ArrayList<>();
        if(bannerInfos.size() > 0){
            try{
                for(BannerInfo bannerInfo : bannerInfos){
                    BannerInfoVo result = new BannerInfoVo();
                    BeanUtils.copyProperties(result,bannerInfo);
                    result.setBannerUrl(uploadProperties.getUrl(result.getBannerUrl()));
                    resultList.add(result);
                }
            }catch (IllegalAccessException e) {
                log.error(" bannerInfo get all failed : e:{} ",e);
                e.printStackTrace();
                throw new RuntimeException();
            } catch (InvocationTargetException e) {
                log.error(" bannerInfo get all failed : e:{} ",e);
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        return resultList;
    }

}
