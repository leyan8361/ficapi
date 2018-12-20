package com.fic.service.service.impl;

import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.BetMovie;
import com.fic.service.entity.BoxOffice;
import com.fic.service.mapper.BetMovieMapper;
import com.fic.service.mapper.BetScenceMovieMapper;
import com.fic.service.mapper.BoxOfficeMapper;
import com.fic.service.service.MaoYanService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.OkHttpUtil;
import com.fic.service.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    BetMovieMapper betMovieMapper;

    @Override
    public Map<String,BoxOffice> getDataByDate(String date) {
        Map<String,BoxOffice> resultList = new HashMap<String,BoxOffice>();
        JSONObject result = okHttpUtil.get(serverProperties.getMaoYanUrl()+"?beginDate="+ date);
        System.out.println(serverProperties.getMaoYanUrl()+"?beginDate="+ date);
        if(null == result){
            log.error(" 抓取猫眼数据 失败 时间 :{}",new Date());
        }
        JSONObject data  = result.getJSONObject("data");
        String updateTime = data.getString("updateInfo");
        System.out.println("最后更新时间 : " + updateTime);
        JSONArray list = data.getJSONArray("list");
        for(Object s: list){
            JSONObject item = (JSONObject)s;
            BoxOffice boxOffice = new BoxOffice();
            //电影名
            String movieNameStr = item.getString("movieName");
            //今日票房
            String boxOfficeStr = item.getString("boxInfo");
            //上映天数
            String releaseDayStr = item.getString("releaseInfo");
            //总票房
            String sumBoxInfoStr = item.getString("sumBoxInfo");

            try{
                //截取今天票房单位
                String boxInfoUnit = RegexUtil.getChinese(boxOfficeStr);
                //rid 万字
                String boxOfficeCut = RegexUtil.ridChiese(boxOfficeStr);
                //转型
                BigDecimal boxOfficeDec = new BigDecimal(boxOfficeCut);
                //截取总票房单位
                String sumBoxInfoUnit = RegexUtil.getChinese(sumBoxInfoStr);
                //rid总票房单位
                String sumBoxInfoCut= RegexUtil.ridChiese(sumBoxInfoStr);
                BigDecimal sumBox = new BigDecimal(sumBoxInfoCut);
                boxOffice.setBoxInfoUnit(StringUtils.isEmpty(boxInfoUnit)?"万":boxInfoUnit);
                boxOffice.setReleaseDay(releaseDayStr);
                boxOffice.setSumBoxInfo(sumBox);
                boxOffice.setSumBoxInfoUnit(sumBoxInfoUnit);
                boxOffice.setMovieName(movieNameStr);
                boxOffice.setBoxInfo(boxOfficeDec);
                resultList.put(movieNameStr,boxOffice);
            }catch (Exception e){
                log.error(" 猫眼抓取，  数据转型失败 e :{}",e);
            }
            System.out.println("电影名 ( " + movieNameStr + " )  ----->  今日票房("+ boxOfficeStr + "万)  ------> 天数 : " + releaseDayStr + "  ------->  总票房(" + sumBoxInfoStr +")");
        }
        return resultList;
    }
}
