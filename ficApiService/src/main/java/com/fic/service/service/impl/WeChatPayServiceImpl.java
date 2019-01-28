package com.fic.service.service.impl;

import com.fic.service.Enum.*;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.Vo.WcPreOrderResultVo;
import com.fic.service.constants.WeChatProperties;
import com.fic.service.entity.BalanceStatement;
import com.fic.service.entity.ExchangeRate;
import com.fic.service.entity.Invest;
import com.fic.service.entity.WxPayInfo;
import com.fic.service.mapper.BalanceStatementMapper;
import com.fic.service.mapper.ExchangeRateMapper;
import com.fic.service.mapper.InvestMapper;
import com.fic.service.mapper.WxPayInfoMapper;
import com.fic.service.service.WeChatPayService;
import com.fic.service.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 1)创建预订单
 * 2)App 使用订单号请求支付
 * 3)回调、查询订单支付状态
 * 4)支付成功增加（资产）及（余额变更记录）
 */
@Service
public class WeChatPayServiceImpl implements WeChatPayService {

    private final Logger log = LoggerFactory.getLogger(WeChatPayServiceImpl.class);

    private static final String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String WX_CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    OkHttpUtil okHttpUtil;
    @Autowired
    WxPayInfoMapper wxPayInfoMapper;
    @Autowired
    InvestMapper investMapper;
    @Autowired
    ExchangeRateMapper exchangeRateMapper;
    @Autowired
    BalanceStatementMapper balanceStatementMapper;

    @Override
    public ResponseVo wxPay(String total_fee, String ip,Integer userId) {
        log.debug("创建预支付订单 totalFee:{}, imei:{},ip:{},openId:{},user id :{}",total_fee,ip,userId);

        /** 1.封装 Param */
        WxPayInfo wxPayInfo = new WxPayInfo();
        Map<String,String> payParam = new HashMap<>();
        String orderNum = getOrderNum();
        String nonceStr = getNonceStr();
        payParam.put("appid", weChatProperties.getAppId()); //TODO 小程序 aapid
        payParam.put("attach", orderNum);//附加数据
        payParam.put("body", "TFC");//商品描述
        payParam.put("device_info", "APP");//
        payParam.put("mch_id", weChatProperties.getPartnerId());//TODO 商品号
        payParam.put("nonce_str", nonceStr);//随机字符串
        payParam.put("notify_url", weChatProperties.getNotifyUrl());//TODO 异步通知URL
        payParam.put("out_trade_no", orderNum);//订单号
        payParam.put("sign_type", "MD5");
        payParam.put("spbill_create_ip", ip);
        payParam.put("total_fee", total_fee);
        payParam.put("trade_type", "APP");

        /** 2.签名 */
        String sign = WxPaySignatureUtils.signatureSHA1(payParam);
        payParam.put("sign", sign);

        String xml = XmlUtil.mapToXml(payParam);
        /** 3.发送 */
        String resultStr = okHttpUtil.postForWxPay(WX_PAY_URL,xml);
        if(null == resultStr){
            log.error("微信支付，创建预支付订单失败");
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_FAILED,null);
        }
        Map<String,String> resultMap = XmlUtil.xmlToMap(resultStr);

        /** 4.解析请求结果Status */
        if(resultMap.get("return_code").indexOf("SUCCESS") != -1) {
            /** 请求失败 */
            log.error("微信支付，创建预支付订单失败 result xml : {}", resultStr);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_FAILED, null);
        }
        if(resultMap.get("result_code").indexOf("SUCCESS") != -1){
            /** 请求失败 */
            log.error("微信支付，创建预支付订单失败 result xml : {}", resultStr);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_FAILED, null);
        }

        /** 5.验证签名 */
        String returnSign = resultMap.get("sign");
        if(!returnSign.equals(sign)){
            /**验证签名失败*/
            log.error("微信支付，创建预支付订单，验证签名失败 result xml :{}, request xml :{}",resultStr,xml);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_FAILED, null);
        }

        /** 6.保存预订单数据 */
        wxPayInfo.setStatus(WxOrderStatusEnum.BUILD_ORDER_SUCCESS.code());
        wxPayInfo.setPayStatus(WxPayStatusEnum.NOTPAY.code());
        wxPayInfo.setPrepayId(resultMap.get("prepay_id"));//微信支付标识，用于app请求支付
        wxPayInfo.setOrderNum(orderNum);
        wxPayInfo.setCreatedTime(new Date());
        wxPayInfo.setRequestBody(xml);
        wxPayInfo.setResponseBody(resultStr);
        wxPayInfo.setNoncestr(nonceStr);
        wxPayInfo.setSign(sign);
        wxPayInfo.setUserId(userId);
        int savePayInfo = wxPayInfoMapper.insertSelective(wxPayInfo);
        if(savePayInfo <=0){
            log.error("微信支付，创建预支付订单失败");
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_FAILED,null);
        }
        /** 7.组装App 请求支付参数 */
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
        /** 1.查找预订单数据 封装 Param */
        WxPayInfo findResult = wxPayInfoMapper.findByOrderNum(orderNum);
        if(null == findResult){
            log.debug("查询订单状态失败，order Num :{}",orderNum);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_ORDER_NOT_EXIST,null);
        }
        Map<String,String> payParam = new HashMap<>();
        payParam.put("appid", weChatProperties.getAppId()); //TODO 小程序 aapid
        payParam.put("mch_id", weChatProperties.getPartnerId());//TODO 商品号
        payParam.put("transaction_id", findResult.getTransactionId());//微信支付订单号
        payParam.put("nonce_str", findResult.getNoncestr());
        payParam.put("out_trade_no", orderNum);//订单号
        /** 2.签名 */
        String sign = WxPaySignatureUtils.signatureSHA1(payParam);
        payParam.put("sign", sign);
        String xml = XmlUtil.mapToXml(payParam);
        /** 3.发送 */
        String resultStr = okHttpUtil.postForWxPay(WX_CHECK_ORDER_URL,xml);
        if(null == resultStr){
            log.error("微信支付，查询订单失败");
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_FAILED,null);
        }
        Map<String,String> resultMap = XmlUtil.xmlToMap(resultStr);
        /** 4.解析请求结果Status */
        if(resultMap.get("return_code").indexOf("SUCCESS") != -1) {
            /** 请求失败 */
            log.error("微信支付，查询订单失败 result xml : {}", resultStr);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_QUERY_FAILED, null);
        }
        if(resultMap.get("result_code").indexOf("SUCCESS") != -1){
            /** 请求失败 */
            log.error("微信支付，查询订单失败 result xml : {}", resultStr);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_QUERY_FAILED, null);
        }
        /** 5.验证签名 */
        String returnSign = resultMap.get("sign");
        if(!returnSign.equals(sign)){
            /**验证签名失败*/
            log.error("微信支付，查询订单失败，验证签名失败 result xml :{}, request xml :{}",resultStr,xml);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_QUERY_FAILED, null);
        }
        /** 6.解析订单结果 */
        String tradeState = resultMap.get("trade_state");
        WxPayStatusEnum payStatusEnum = WxPayStatusEnum.valueOf(tradeState);
        String timeEnd = resultMap.get("time_end");//支付完成时间
        String transactionId = resultMap.get("transaction_id");//微信支付订单号
        String totalFee = resultMap.get("total_fee");//标价金额
        switch (payStatusEnum){
            case NOTPAY:
                log.debug("微信支付，查询订单失败, not pay , order num :{}",orderNum);
                break;
            case SUCCESS:
                //支付成功
                doPayment(findResult.getUserId(),totalFee,findResult.getId());
                findResult.setTransactionId(transactionId);
                findResult.setPayStatus(WxPayStatusEnum.SUCCESS.code());
                findResult.setStatus(WxOrderStatusEnum.SUCCESS.code());
                findResult.setNotifyTime(DateUtil.strMatSecToDate(timeEnd));
                break;
            case USERPAYING:
                //支付中
                findResult.setPayStatus(WxPayStatusEnum.USERPAYING.code());
                break;
            case PAYERROR:
                //支付失败
                findResult.setPayStatus(WxPayStatusEnum.PAYERROR.code());
                break;
            case REFUND:
                //转入退款
                findResult.setPayStatus(WxPayStatusEnum.REFUND.code());
                break;
            case CLOSED:
                //已关闭
                findResult.setPayStatus(WxPayStatusEnum.CLOSED.code());
                break;
                default:
                    log.error("查询微信支付，无对应状态 pay status :{}",payStatusEnum.code());
                    break;
        }
        int updateWxPayInfo = wxPayInfoMapper.updateByPrimaryKeySelective(findResult);
        if(updateWxPayInfo <=0){
            log.error("查询微信支付，更新wx pay info 失败 wx pay id :{}",findResult.getId());
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
    }

    public void doPayment(Integer userId,String amount,Integer wxPayId){
        Invest invest = investMapper.findByUserId(userId);
        if(null == invest){
            log.error("查询微信支付到账失败，到账成功，处理用户资产失败，user id :{}",userId);
            throw new RuntimeException();
        }
        ExchangeRate exchangeRate = exchangeRateMapper.findFicExchangeCny();
        if(null == exchangeRate){
            log.error("查询微信支付，兑换TFC失败，无汇率信息");
            throw new RuntimeException();
        }
        BigDecimal amountDecimal = new BigDecimal(amount);//单位为分
        if(amountDecimal.compareTo(BigDecimal.ZERO) <= 0){
            log.error("查询微信支付到账失败，无效金额 amout :{}",amount);
            throw new RuntimeException();
        }
        BigDecimal realAmount = amountDecimal.divide(new BigDecimal("100"));
        BigDecimal tfcTransferAmount = realAmount.divide(exchangeRate.getRate()).setScale(0,BigDecimal.ROUND_DOWN);
        log.debug("查询微信支付，实际到账，金额 :{},tfc :{}",amount,tfcTransferAmount);
        BalanceStatement balanceStatement = new BalanceStatement();
        balanceStatement.setUserId(userId);
        balanceStatement.setWay(FinanceWayEnum.IN.getCode());
        balanceStatement.setAmount(tfcTransferAmount);
        balanceStatement.setType(FinanceTypeEnum.RECHARGE.getCode());
        balanceStatement.setCreatedTime(new Date());
        balanceStatement.setTraceId(wxPayId);
        int saveBalanceResult = balanceStatementMapper.insertSelective(balanceStatement);
        if(saveBalanceResult <=0){
            log.error("查询微信支付，生成余额变更记录失败 user id :{}, amount :{},wxPay id :{}",userId,amount,wxPayId);
            throw new RuntimeException();
        }
        BigDecimal balance = invest.getBalance().add(tfcTransferAmount);
        int updateInvestResult = investMapper.updateBalance(balance,userId);
        if(updateInvestResult <=0){
            log.error("查询微信支付，更新用户资产 user id :{}, amount :{},wxPay id :{}",userId,amount,wxPayId);
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseVo notify(String xml) {
        log.debug("异步通知 开始 service");
        Map<String,String> resultMap = XmlUtil.xmlToMap(xml);
        String orderNum = resultMap.get("out_trade_no");
        /** 1.查询预订单信息 */
        WxPayInfo findResult = wxPayInfoMapper.findByOrderNum(orderNum);
        if(null == findResult){
            log.error("异步通知 无效商家订单号 order num :{}",orderNum);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }
        String sign = resultMap.get("sign");
        /** 2.验签 */
        if(!sign.equals(findResult.getSign())){
            log.error("异步通知 验签失败 order num :{}",orderNum);
            return new ResponseVo(ErrorCodeEnum.SYSTEM_EXCEPTION,null);
        }

        String timeEnd = resultMap.get("time_end");//支付完成时间
        String transactionId = resultMap.get("transaction_id");//微信支付订单号
        String totalFee = resultMap.get("total_fee");//标价金额

        /** 3.解析请求结果Status */
        if(resultMap.get("return_code").indexOf("SUCCESS") != -1) {
            /** 请求失败 */
            log.error("异步通知 微信支付，查询订单失败 result xml : {}", xml);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_QUERY_FAILED, null);
        }
        if(resultMap.get("result_code").indexOf("SUCCESS") != -1){
            /** 请求失败 */
            log.error("异步通知 微信支付，查询订单失败 result xml : {}", xml);
            return new ResponseVo(ErrorCodeEnum.WE_CHAT_PAY_QUERY_FAILED, null);
        }

        /** 4.处理结果 */
        //支付成功
        doPayment(findResult.getUserId(),totalFee,findResult.getId());
        findResult.setTransactionId(transactionId);
        findResult.setPayStatus(WxPayStatusEnum.SUCCESS.code());
        findResult.setStatus(WxOrderStatusEnum.SUCCESS.code());
        findResult.setNotifyTime(DateUtil.strMatSecToDate(timeEnd));
        int updateWxPayInfo = wxPayInfoMapper.updateByPrimaryKeySelective(findResult);
        if(updateWxPayInfo <=0){
            log.error("异步通知 查询微信支付，更新wx pay info 失败 wx pay id :{}",findResult.getId());
            throw new RuntimeException();
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,null);
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
