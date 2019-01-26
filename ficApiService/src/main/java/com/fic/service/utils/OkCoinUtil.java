package com.fic.service.utils;

import com.fic.service.Enum.CoinUSDEnum;
import com.fic.service.constants.Constants;
import com.fic.service.entity.TickerRecord;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

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

    public TickerRecord getTicker(CoinUSDEnum coinUSDEnum){
        log.debug(" get ticker");
        String path = "/api/spot/v3/instruments/"+ coinUSDEnum.code() +"/ticker";
        String queryString = "";
        String result = okHttpUtil.getForOkCoin(path,queryString);
        if(StringUtils.isEmpty(result)){
            return null;
        }
        TickerRecord record = new TickerRecord();
        JSONObject jsonObject =  new JSONObject(result);
        record.setBestAsk(jsonObject.get("best_ask").toString());//卖一价
        record.setBestBid(jsonObject.get("best_bid").toString());//买一价
        record.setInstrumentId(coinUSDEnum.code());
        record.setOpen24h(jsonObject.get("open_24h").toString());//24小时开盘价
        record.setHigh24h(jsonObject.get("high_24h").toString());//24小时最高价
        record.setLow24h(jsonObject.get("low_24h").toString());//24小时最低价
        record.setLast(jsonObject.get("last").toString());//最新成交价
        record.setTimestamp(jsonObject.get("timestamp").toString());//系统时间戳
        record.setCreatedTime(new Date());

        switch (coinUSDEnum){
            case USDT:
                record.setCoinType(Constants.USDT);
                break;
            case BCH:
                record.setCoinType(Constants.BCH);
                break;
            case BTC:
                record.setCoinType(Constants.BTC);
                break;
            case ETH:
                record.setCoinType(Constants.ETH);
                break;
                default:
                    break;
        }
        return record;
    }


}
