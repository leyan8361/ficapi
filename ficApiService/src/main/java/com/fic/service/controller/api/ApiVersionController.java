package com.fic.service.controller.api;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.InvestBalanceInfoVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.Vo.VersionInfoVo;
import com.fic.service.constants.UploadProperties;
import com.fic.service.controller.HomeController;
import com.fic.service.entity.AppVersion;
import com.fic.service.mapper.AppVersionMapper;
import com.fic.service.utils.RegexUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *   @Author Xie
 *   @Date 2018/11/29
 *   @Discription:
**/
@RestController
@RequestMapping("/api/v1")
@Api(description = "Api-版本检测")
public class ApiVersionController {

    private final Logger log = LoggerFactory.getLogger(ApiVersionController.class);

    @Autowired
    AppVersionMapper appVersionMapper;
    @Autowired
    UploadProperties uploadProperties;

    @GetMapping("/checkVersion")
    @ApiOperation("Api-版本检测")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "version", value = "版本号", required = true,example = "vX.X.X,vXX.XX.XX"),
            @ApiImplicitParam(dataType = "byte", name = "deviceType", value = "客户端类型 [0,android],[1,ios]", required = true, example = "0")
    })
    @ApiResponses({
            @ApiResponse(code = 3200, message = "VERSION ILLEGAL"),
            @ApiResponse(code = 200, message = "SUCCESS",response = VersionInfoVo.class)
    })
    public ResponseEntity checkVersion(@RequestParam String version,@RequestParam byte deviceType) {
        log.debug(" Version Check Action !!!");
        int versionCode = RegexUtil.isVersion(version);
        if(0 == versionCode)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.VERSION_ILLEGAL,null));
        AppVersion appVersion = appVersionMapper.findByDeviceType(deviceType);
        if(null == appVersion)return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.VERSION_ILLEGAL,null));
        int lastedVersionCode = RegexUtil.isVersion(appVersion.getVersion());
        VersionInfoVo versionInfoVo = new VersionInfoVo();
        if(lastedVersionCode <= versionCode){
            versionInfoVo.setNeedToUpdate(false);
            log.debug(" the current version is lasted ! version:{}",version);
            return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,versionInfoVo));
        }
        versionInfoVo.setLastedVersionDownloadUrl(uploadProperties.getUrl(appVersion.getDownloadUrl()));
        if(appVersion.getDeviceType()==1){
         versionInfoVo.setLastedVersionDownloadUrl("https://www.pgyer.com/taoying");
        }
        versionInfoVo.setLastedVersion(appVersion.getVersion());
        versionInfoVo.setNeedToUpdate(true);
        return ResponseEntity.ok(new ResponseVo(ErrorCodeEnum.SUCCESS,versionInfoVo));
    }

}
