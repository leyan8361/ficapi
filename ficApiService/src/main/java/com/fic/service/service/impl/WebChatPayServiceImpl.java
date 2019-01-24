package com.fic.service.service.impl;

import com.fic.service.service.WebChatPayService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class WebChatPayServiceImpl implements WebChatPayService {

    @Override
    public JSONObject wxPay(String total_fee, String imei, String ip, String openid) {
        return null;
    }
}
