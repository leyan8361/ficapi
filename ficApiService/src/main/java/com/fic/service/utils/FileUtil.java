package com.fic.service.utils;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.UploadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author Xie
 * @Date $date$
 * @Description: FileUtil
 **/
@Component
public class FileUtil {

    private final Logger log = LoggerFactory.getLogger(FileUtil.class);

    @Autowired
    UploadProperties uploadProperties;

    /**
     *
     * @param uploadFile
     * @param path include fileName and fileType
     * @return
     */
    public ErrorCodeEnum saveFile(MultipartFile uploadFile,String path,String name){
        try{

            File file = new File(uploadProperties.getCurrentUploadPath()+path);
            //删除所有旧文件
            this.deleteAll(file);
            file.mkdirs();

            file = new File(uploadProperties.getCurrentUploadPath()+path+name);
            uploadFile.transferTo(file);
            return ErrorCodeEnum.SUCCESS;
        }catch (IOException e){
            e.printStackTrace();
            log.error("上传失败 : filePath : {}",uploadProperties.getCurrentUploadPath()+path+name);
            return ErrorCodeEnum.USER_HEAD_PIC_UPLOAD_FAILED;
        }
    }

    public void deleteAll(File f){
        File [] b = f.listFiles();
        for(int i =0;i<b.length;i++){
            if(b[i].isFile()){
                b[i].delete();
            }else{
                deleteAll(b[i]);
            }
        }
        f.delete();
    }

}
