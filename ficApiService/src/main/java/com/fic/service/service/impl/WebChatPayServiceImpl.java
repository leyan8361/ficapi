package com.fic.service.service.impl;

import com.fic.service.constants.WeChatProperties;
import com.fic.service.service.WebChatPayService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.MD5Util;
import com.fic.service.utils.WxPaySignatureUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.UUID;

@Service
public class WebChatPayServiceImpl implements WebChatPayService {

    private final Logger log = LoggerFactory.getLogger(WebChatPayServiceImpl.class);

    @Autowired
    WeChatProperties weChatProperties;

    @Override
    public JSONObject wxPay(String total_fee, String imei, String ip, String openid) {
        LinkedHashMap payParam = new LinkedHashMap<>();
        String orderNum = getOrderNum();
        payParam.put("appid", weChatProperties.getAppId()); //小程序 aapid
        payParam.put("attach", orderNum);//附加数据
        payParam.put("body", "淘影充值");//商品描述
        payParam.put("device_info", "WEB");//
        payParam.put("mch_id", this.PAY_MACH_ID);//商品号
        payParam.put("nonce_str", getNonceStr());//随机字符串
        payParam.put("notify_url", weChatProperties.getNotifyUrl());//返回连接
        payParam.put("openid", openid);//微信用户 id
        payParam.put("out_trade_no", orderNum);//订单号
        payParam.put("sign_type", "MD5");
        payParam.put("spbill_create_ip", ip);
        payParam.put("total_fee", total_fee);
        payParam.put("trade_type", "APP");
        payParam.put("sign", WxPaySignatureUtils.signature());
        return null;
    }

    private static String getOrderNum(){
        String time = DateUtil.getTimeStamp();
        UUID uuNum = UUID.nameUUIDFromBytes(time.getBytes());
        String num = "TAOYING" + uuNum.toString();
        return num;
    }

    private static String getNonceStr(){
        return MD5Util.MD5(UUID.randomUUID().toString());
    }

    public static void main(String args[]){
        System.out.println(getNonceStr());
    }

}
