package com.fic.service.service.impl;

import com.fic.service.Enum.BooleanStatusEnum;
import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.Vo.WcPreOrderResultVo;
import com.fic.service.constants.WeChatProperties;
import com.fic.service.entity.WxPayInfo;
import com.fic.service.mapper.WxPayInfoMapper;
import com.fic.service.service.WebChatPayService;
import com.fic.service.utils.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WebChatPayServiceImpl implements WebChatPayService {

    private final Logger log = LoggerFactory.getLogger(WebChatPayServiceImpl.class);

    private static final String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String WX_CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    OkHttpUtil okHttpUtil;
    @Autowired
    WxPayInfoMapper wxPayInfoMapper;

    @Override
    public ResponseVo wxPay(String total_fee, String imei, String ip, String openid) {
        log.debug("创建预支付订单 totalFee:{}, imei:{},ip:{},openId:{}",total_fee,imei,ip,openid);
        WxPayInfo wxPayInfo = new WxPayInfo();
        Map<String,String> payParam = new HashMap<>();
        String orderNum = getOrderNum();
        String nonceStr = getNonceStr();
        payParam.put("appid", weChatProperties.getAppId()); //TODO 小程序 aapid
        payParam.put("attach", orderNum);//附加数据
        payParam.put("body", "淘影充值");//商品描述
        payParam.put("device_info", "APP");//
        payParam.put("mch_id", weChatProperties.getPartnerId());//TODO 商品号
        payParam.put("nonce_str", nonceStr);//随机字符串
        payParam.put("notify_url", weChatProperties.getNotifyUrl());//TODO 异步通知URL
        payParam.put("openid", openid);//TODO 微信用户 id
        payParam.put("out_trade_no", orderNum);//订单号
        payParam.put("sign_type", "MD5");
        payParam.put("spbill_create_ip", ip);
        payParam.put("total_fee", total_fee);
        payParam.put("trade_type", "APP");
        String sign = WxPaySignatureUtils.signatureSHA1(payParam);
        payParam.put("sign", sign);

        String xml = XmlUtil.mapToXml(payParam);
        String resultStr = okHttpUtil.postForWxPay(WX_PAY_URL,xml);
        if(null == resultStr){
            log.error("微信支付，创建预支付订单失败");
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_FAILED,null);
        }
        Map<String,String> resultMap = XmlUtil.xmlToMap(resultStr);

        if(resultMap.get("return_code").indexOf("SUCCESS") != -1){
            wxPayInfo.setStatus(BooleanStatusEnum.NO.code());
        }else{
            wxPayInfo.setStatus(BooleanStatusEnum.YES.code());
            wxPayInfo.setPrepayId(resultMap.get("prepay_id"));//微信支付标识，用于app请求支付
        }
        wxPayInfo.setOrderNum(orderNum);
        wxPayInfo.setCreatedTime(new Date());
        wxPayInfo.setRequestBody(xml);
        wxPayInfo.setResponseBody(resultStr);
        wxPayInfo.setNoncestr(nonceStr);
        wxPayInfo.setSign(sign);
        int savePayInfo = wxPayInfoMapper.insertSelective(wxPayInfo);
        if(savePayInfo <=0){
            log.error("微信支付，创建预支付订单失败");
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_FAILED,null);
        }
        WcPreOrderResultVo result = new WcPreOrderResultVo();
        result.setAppId(weChatProperties.getAppId());
        result.setNoncestr(nonceStr);
        result.setPartnerid(weChatProperties.getPartnerId());
        result.setPrepayid(wxPayInfo.getPrepayId());
        result.setSign(sign);
        result.setTimestamp(DateUtil.getTimeStamp());
        return new ResponseVo(ErrorCodeEnum.SUCCESS,result);
    }

    @Override
    public ResponseVo wxCheckOrder(String orderNum) {
        log.debug("查询订单 order num:{}",orderNum);
        WxPayInfo findResult = wxPayInfoMapper.findByOrderNum(orderNum);
        if(null == findResult){
            log.debug("查询订单状态失败，order Num :{}",orderNum);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_ORDER_NOT_EXIST,null);
        }
        Map<String,String> payParam = new HashMap<>();
        payParam.put("appid", weChatProperties.getAppId()); //TODO 小程序 aapid
        payParam.put("mch_id", weChatProperties.getPartnerId());//TODO 商品号
        payParam.put("transaction_id", findResult.getTransactionId());
        payParam.put("nonce_str", findResult.getNoncestr());
        payParam.put("out_trade_no", orderNum);//订单号
        String sign = WxPaySignatureUtils.signatureSHA1(payParam);
        payParam.put("sign", sign);
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
