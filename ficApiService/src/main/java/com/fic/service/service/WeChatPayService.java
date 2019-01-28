package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;

public interface WeChatPayService {

    /**
     * 统计下单
     */
    ResponseVo wxPay(String total_fee, String ip,Integer userId);

    /**
     * 查询订单
     */
    ResponseVo wxCheckOrder(String order_num);

    /**
     * 异步通知
     */
    ResponseVo notify(String xml);
}
