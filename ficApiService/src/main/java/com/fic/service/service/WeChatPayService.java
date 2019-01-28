package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;

public interface WeChatPayService {

    /**
     * 预下单
     */
    ResponseVo wxPay(String total_fee, String ip,Integer userId);

    /**
     * 申请退款
     */
    ResponseVo wxRefund(String orderNum);

    /**
     * 查询订单
     */
    ResponseVo wxCheckOrder(String order_num);

    /**
     * 查询 退款订单
     */
    ResponseVo wxCheckRefund(String refundNo);

    /**
     * 到账异步通知
     */
    ResponseVo notify(String xml);

    /**
     * 退款异步通知
     */
    ResponseVo notifyRefund(String xml);




}
