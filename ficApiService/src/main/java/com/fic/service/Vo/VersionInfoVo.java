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
    @ApiModelProperty(value = "是否需要更新")
    private boolean isNeedToUpdate;

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

    public VersionInfoVo(String lastedVersion, boolean isNeedToUpdate) {
        this.lastedVersion = lastedVersion;
        this.isNeedToUpdate = isNeedToUpdate;
    }

    public VersionInfoVo() {
    }
}
