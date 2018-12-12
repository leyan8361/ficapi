package com.fic.service.service.impl;

import com.fic.service.constants.ServerProperties;
import com.fic.service.service.MaoYanService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.OkHttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/12/12
 *   @Discription:
**/
@Service
public class MaoYanServiceImpl implements MaoYanService {

    private final Logger log = LoggerFactory.getLogger(MaoYanServiceImpl.class);

    @Autowired
    OkHttpUtil okHttpUtil;
    @Autowired
    ServerProperties serverProperties;

    @Override
    public void getNewestData() {
        JSONObject result = okHttpUtil.get(serverProperties.getMaoYanUrl()+"??beginDate="+ DateUtil.getYesterdayAndFormatDay());
        if(null == result){
            log.error(" 抓取猫眼数据 失败 时间 :{}",new Date());
        }
        JSONObject data  = result.getJSONObject("data");
        String updateTime = data.getString("updateInfo");
        System.out.println("最后更新时间 : " + updateTime);
        JSONArray list = data.getJSONArray("list");
        for(Object s: list){
            JSONObject item = (JSONObject)s;
            //电影名
            String movieName = item.getString("movieName");
            //今日票房
            String boxOffice = item.getString("boxInfo");
            //上映天数
            String releaseDay = item.getString("releaseInfo");
           //总票房
            String sumBoxInfo = item.getString("sumBoxInfo");

            System.out.println("电影名 ( " + movieName + " )  ----->  今日票房("+ boxOffice + "万)  ------> 天数 : " + releaseDay + "  ------->  总票房(" + sumBoxInfo +")");
        }

    }
}