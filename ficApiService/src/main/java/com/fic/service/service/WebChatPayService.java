package com.fic.service.service;


import com.fic.service.entity.WxPayInfo;

public interface WebChatPayService {

    /**
     * 统计下单
     */
    WxPayInfo wxPay(String total_fee, String imei, String ip, String openid);
}
