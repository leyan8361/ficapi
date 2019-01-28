package com.fic.service.scheduled;

import com.fic.service.Enum.WxPayStatusEnum;
import com.fic.service.Enum.WxRefundStatusEnum;
import com.fic.service.entity.WxPayInfo;
import com.fic.service.mapper.WxPayInfoMapper;
import com.fic.service.service.WeChatPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeChatScheduledService {

    private final Logger log = LoggerFactory.getLogger(WeChatScheduledService.class);

    @Autowired
    WxPayInfoMapper wxPayInfoMapper;
    @Autowired
    WeChatPayService weChatPayService;

    /**
     * 轮询订单
     */
//    @Scheduled(cron = "*/30 * * * * ?")
    public void checkWeChatOrder(){
        log.debug("轮询，微信订单");
        List<WxPayInfo> payList = wxPayInfoMapper.findAllByPayStatus(WxPayStatusEnum.NOTPAY.code());
        List<WxPayInfo> refundList = wxPayInfoMapper.findAllByRefundStatus(WxRefundStatusEnum.PROCESSING.code());
        if(payList.size() > 0){
            for(WxPayInfo wx : payList){
                weChatPayService.wxCheckOrder(wx.getOrderNum());
            }
        }
        if(refundList.size() > 0){
            for(WxPayInfo wx : payList){
                weChatPayService.wxCheckRefund(wx.getRefundNo());
            }
        }

    }
}
