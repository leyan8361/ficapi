package com.fic.service.service;


import com.fic.service.Vo.ResponseVo;

public interface WebChatPayService {

    /**
     * 统计下单
     */
    ResponseVo wxPay(String total_fee, String imei, String ip, String openid);

    /**
     * 查询订单
     */
    ResponseVo wxCheckOrder(String order_num);
}
