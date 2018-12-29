package com.fic.service.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@ApiModel
public class VersionInfoVo {

    @ApiModelProperty(value = "版本号")
    private String lastedVersion;
    @ApiModelProperty(value = "是否需要更新,true为需要更新,false不需要更新")
    private boolean isNeedToUpdate;
    @ApiModelProperty(value = "需要更新时的下载链接")
    private String lastedVersionDownloadUrl;

    public String getLastedVersion() {
        return lastedVersion;
    }

    public void setLastedVersion(String lastedVersion) {
        this.lastedVersion = lastedVersion;
    }

    public boolean isNeedToUpdate() {
        return isNeedToUpdate;
    }

    public void setNeedToUpdate(boolean needToUpdate) {
        isNeedToUpdate = needToUpdate;
    }

    public String getLastedVersionDownloadUrl() {
        return lastedVersionDownloadUrl;
    }

    public void setLastedVersionDownloadUrl(String lastedVersionDownloadUrl) {
        this.lastedVersionDownloadUrl = lastedVersionDownloadUrl;
    }

    public VersionInfoVo(String lastedVersion, boolean isNeedToUpdate, String lastedVersionDownloadUrl) {
        this.lastedVersion = lastedVersion;
        this.isNeedToUpdate = isNeedToUpdate;
        this.lastedVersionDownloadUrl = lastedVersionDownloadUrl;
    }

    public VersionInfoVo() {
    }
}
