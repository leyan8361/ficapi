package com.fic.service.utils;

import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *   @Author Xie
 *   @Date 2018/12/11
 *   @Discription:
**/
@Component
public class OkHttpUtil {

    private final Logger log = LoggerFactory.getLogger(Web3jUtil.class);

    private OkHttpClient client = null;

    @PostConstruct
    public void init(){
        if(null == client){
            synchronized (OkHttpUtil.class){
                client = new OkHttpClient().newBuilder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS).build();
                log.debug(" HttpClient Build!");
            }
        }
    }

    @PreDestroy
    public void  dostory(){
        client = null;
    }

    public JSONObject get(String url){
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        JSONObject result = null;
        try{
            Response response = call.execute();
            if(response.isSuccessful()){
                String resultStr = response.body().string();
                result = new JSONObject(resultStr);
            }
            response.close();
        }catch(IOException io){
            log.error("okhttpClient exception, get url :{}",url);
            io.printStackTrace();
        }
        return result;
    }

    public JSONObject post(String url,FormBody params){
        Request request = new Request.Builder().url(url).post(params).build();
        Call call = client.newCall(request);
        JSONObject result = null;
        try{
            Response response = call.execute();
            if(response.isSuccessful()){
                String resultStr = response.body().string();
                result = new JSONObject(resultStr);
            }
            response.close();
        }catch(IOException io){
            log.error("okhttpClient exception,post  url :{}",url);
            io.printStackTrace();
        }
        return result;
    }

    /**
     *
     * 解析范例
     * JSONObject result = okHttpUtil.get("https://api.shenjian.io/?appid=90b029a1508fcccfecb5bc2211da4239");
     *         if(null!= result){
     *             JSONArray array = result.getJSONArray("data");
     *             for(Object s: array){
     *                 JSONObject item = (JSONObject)s;
     *                 String movieName = item.get("MovieName").toString();
     *                 String boxOffice = item.get("BoxOffice").toString();
     *                 System.out.println("movieName : " + movieName + " | boxOffice : "+ boxOffice);
     *             }
     *         }
     *
     */

}
