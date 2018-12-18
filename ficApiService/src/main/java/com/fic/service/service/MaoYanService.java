package com.fic.service.service;

import com.fic.service.entity.BoxOffice;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 *   @Author Xie
 *   @Date 2018/12/12
 *   @Discription:猫眼抓数据
**/
public interface MaoYanService {

      Map<String,BoxOffice> getDataByDate(String date);
}
