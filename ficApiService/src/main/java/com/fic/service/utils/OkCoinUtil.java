package com.fic.service.utils;

import com.fic.service.Enum.CoinUSDEnum;
import com.fic.service.entity.TickerRecord;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@Component
public class OkCoinUtil {

    private final Logger log = LoggerFactory.getLogger(OkCoinUtil.class);

    @Autowired
    OkHttpUtil okHttpUtil;

    public TickerRecord getTicker(String coinType){
        log.debug(" get ticker");
        String path = "/api/spot/v3/instruments/"+ coinType +"/ticker";
        String queryString = "";
        String result = okHttpUtil.getForOkCoin(path,queryString);
        if(StringUtils.isEmpty(result)){
            return null;
        }
        TickerRecord record = new TickerRecord();
        JSONObject jsonObject =  new JSONObject(result);
        record.setBestAsk(jsonObject.get("best_ask").toString());//卖一价
        record.setBestBid(jsonObject.get("best_bid").toString());//买一价
        record.setInstrumentId(coinType);
        record.setOpen24h(jsonObject.get("open_24h").toString());//24小时开盘价
        record.setHigh24h(jsonObject.get("high_24h").toString());//24小时最高价
        record.setLow24h(jsonObject.get("low_24h").toString());//24小时最低价
        record.setLast(jsonObject.get("last").toString());//最新成交价
        record.setLast(jsonObject.get("timestamp").toString());//系统时间戳
        return record;
    }


}
