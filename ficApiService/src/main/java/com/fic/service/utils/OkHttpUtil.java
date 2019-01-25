package com.fic.service.utils;

import com.fic.service.Enum.OkCoinHeadersEnum;
import com.fic.service.constants.ServerProperties;
import okhttp3.*;
import org.apache.http.client.methods.RequestBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    ServerProperties serverProperties;

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

    /**
     * get for ok coin api
     * @return
     */
    public String getForOkCoin(String path,String queryString){
        String result = "";
        String timestamp = DateUtil.getUnixTime();
        String requestUrl = "";
        try{
            Map<String,String> headersParam = new HashMap<String,String>();
            headersParam.put(OkCoinHeadersEnum.OK_ACCESS_KEY.header(),serverProperties.getOkApiKey());
            headersParam.put(OkCoinHeadersEnum.OK_ACCESS_PASSPHRASE.header(),serverProperties.getOkApiSecretKey());
            headersParam.put(OkCoinHeadersEnum.OK_ACCESS_SIGN.header(),HmacSHA256Base64Utils.sign(timestamp, "GET",path,queryString,"",serverProperties.getOkApiSecretKey()));
            headersParam.put(OkCoinHeadersEnum.OK_ACCESS_TIMESTAMP.header(),timestamp);
            Headers headers = Headers.of(headersParam);
            requestUrl = serverProperties.getOkServerUrl() + path + queryString;
            Request request = new Request.Builder().url(requestUrl).headers(headers).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            if(response.isSuccessful()){
                result = response.body().string();
            }
            response.close();
        }catch(IOException io){
            log.error("okhttpClient exception, get with header url :{}",requestUrl);
            io.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
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

    public String postForWxPay(String url,String xml){
        MediaType xmlContentType = MediaType.parse("application/xml; charset=utf-8");
        RequestBody body = RequestBody.create(xmlContentType,xml);
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        String result = null;
        try{
            Response response = call.execute();
            if(response.isSuccessful()){
                String resultStr = response.body().string();
                result = resultStr;
            }
            response.close();
        }catch(IOException io){
            log.error("okhttpClient wx pay exception,post  url :{}",url);
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
