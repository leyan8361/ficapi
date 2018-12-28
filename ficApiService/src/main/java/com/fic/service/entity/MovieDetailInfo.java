package com.fic.service.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MovieDetailInfo {

    @ApiModelProperty(value = "项目简介")
    private String briefText;
    @ApiModelProperty(value = "项目简介URL")
    private String briefUrl;
    @ApiModelProperty(value = "剧情简介")
    private String plotSummary;
    @ApiModelProperty(value = "剧情简介URL")
    private String plotSummaryUrl;

    public String getBriefText() {
        return briefText;
    }

    public void setBriefText(String briefText) {
        this.briefText = briefText;
    }

    public String getBriefUrl() {
        return briefUrl;
    }

    public void setBriefUrl(String briefUrl) {
        this.briefUrl = briefUrl;
    }

    public String getPlotSummary() {
        return plotSummary;
    }

    public void setPlotSummary(String plotSummary) {
        this.plotSummary = plotSummary;
    }

    public String getPlotSummaryUrl() {
        return plotSummaryUrl;
    }

    public void setPlotSummaryUrl(String plotSummaryUrl) {
        this.plotSummaryUrl = plotSummaryUrl;
    }
}
