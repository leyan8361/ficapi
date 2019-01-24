package com.fic.service.service;


import org.json.JSONObject;

public interface WebChatPayService {

    /**
     * 统计下单
     */
    JSONObject wxPay(String total_fee, String imei, String ip, String openid);
}
