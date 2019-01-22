package com.fic.service.scheduled;

import com.fic.service.Enum.CoinUSDEnum;
import com.fic.service.constants.Constants;
import com.fic.service.entity.TickerRecord;
import com.fic.service.mapper.TickerRecordMapper;
import com.fic.service.utils.OkCoinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@Service
public class MarketScheduledService {

    private final Logger log = LoggerFactory.getLogger(MarketScheduledService.class);

    @Autowired
    TickerRecordMapper tickerRecordMapper;
    @Autowired
    OkCoinUtil okCoinUtil;

//    @Scheduled(cron = "*/30 * * * * ?")
    public void getMarketData(){
        log.debug("更新行情");
        TickerRecord btcResult = okCoinUtil.getTicker(CoinUSDEnum.BTC);
        TickerRecord ethResult = okCoinUtil.getTicker(CoinUSDEnum.ETH);
        TickerRecord usdtResult = okCoinUtil.getTicker(CoinUSDEnum.USDT);
        TickerRecord bchResult = okCoinUtil.getTicker(CoinUSDEnum.BCH);
        if(null != btcResult){
            int saveBtcResult = tickerRecordMapper.insertSelective(btcResult);
            if(saveBtcResult <=0){
                log.error("更新BTC行情失败");
                throw new RuntimeException();
            }
        }
        if(null != ethResult){
            int saveEthResult = tickerRecordMapper.insertSelective(ethResult);
            if(saveEthResult <=0){
                log.error("更新BTH行情失败");
                throw new RuntimeException();
            }
        }
        if(null != usdtResult){
            int saveUsdtResult = tickerRecordMapper.insertSelective(usdtResult);
            if(saveUsdtResult <=0){
                log.error("更新USDT行情失败");
                throw new RuntimeException();
            }
        }
        if(null != bchResult){
            int saveBchResult = tickerRecordMapper.insertSelective(bchResult);
            if(saveBchResult <=0){
                log.error("更新BCH行情失败");
                throw new RuntimeException();
            }
        }
    }

}
