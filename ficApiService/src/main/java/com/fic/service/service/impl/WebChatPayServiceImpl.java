package com.fic.service.service.impl;

import com.fic.service.constants.WeChatProperties;
import com.fic.service.service.WebChatPayService;
import com.fic.service.utils.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WebChatPayServiceImpl implements WebChatPayService {

    private final Logger log = LoggerFactory.getLogger(WebChatPayServiceImpl.class);

    private static final String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    OkHttpUtil okHttpUtil;

    @Override
    public JSONObject wxPay(String total_fee, String imei, String ip, String openid) {
        log.debug("创建预支付订单 totalFee:{}, imei:{},ip:{},openId:{}",total_fee,imei,ip,openid);
        Map<String,String> payParam = new HashMap<>();
        String orderNum = getOrderNum();
        payParam.put("appid", weChatProperties.getAppId()); //小程序 aapid
        payParam.put("attach", orderNum);//附加数据
        payParam.put("body", "淘影充值");//商品描述
        payParam.put("device_info", "WEB");//
        payParam.put("mch_id", weChatProperties.getMerchandiseCode());//商品号
        payParam.put("nonce_str", getNonceStr());//随机字符串
        payParam.put("notify_url", weChatProperties.getNotifyUrl());//异步通知URL
        payParam.put("openid", openid);//微信用户 id
        payParam.put("out_trade_no", orderNum);//订单号
        payParam.put("sign_type", "MD5");
        payParam.put("spbill_create_ip", ip);
        payParam.put("total_fee", total_fee);
        payParam.put("trade_type", "APP");
        payParam.put("sign", WxPaySignatureUtils.signatureSHA1(payParam));

        String xml = XmlUtil.mapToXml(payParam);


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
