package com.fic.service.service.impl;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.constants.SmsProperties;
import com.fic.service.entity.SmsQueue;
import com.fic.service.mapper.SmsQueueMapper;
import com.fic.service.service.SmsService;
import com.fic.service.utils.DateUtil;
import com.fic.service.utils.RandomUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Service
public class SmsServiceImpl implements SmsService {

    private final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    SmsProperties smsProperties;
    @Autowired
    SmsQueueMapper smsQueueMapper;

    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ErrorCodeEnum send(String telephone) {

        SmsQueue sms = new SmsQueue();
        sms.setCode(RandomUtil.generateSmsCode());
        sms.setTelephone(telephone);
        sms.setCreatedTime(new Date());
        sms.setExpiredTime(DateUtil.plusMin(new Date(),smsProperties.getExpired()));
        int smsResult = smsQueueMapper.insert(sms);
        if(smsResult <= 0){
            log.error(" save sms failed! ");
            return ErrorCodeEnum.SYSTEM_EXCEPTION;
        }
        try{
            String[] params = {sms.getCode()};
            SmsSingleSender sender = new SmsSingleSender(smsProperties.getAppId(),smsProperties.getAppKey());
            SmsSingleSenderResult result = sender.sendWithParam("86", telephone,224458, params, "", "", "");
            switch (result.result){
                case 0:
                    return ErrorCodeEnum.SUCCESS;
                case 1016:
                    log.error(" send sms 1016, 手机格式号错误 telephone: " + telephone);
                    return ErrorCodeEnum.INVALID_TELEPHONE;
                case 1014:
                    log.error(" send sms 1014, 模板未通过审核或内容不匹配 telephone: " + telephone);
                    return ErrorCodeEnum.SMS_SEND_REST_ERROR;
                case 1022:
                    log.error(" send sms 1022, 业务短信日下发条数超过设定的上限 telephone: " + telephone);
                    return ErrorCodeEnum.SMS_SEND_COUNT_LIMIT;
                case 1023:
                    log.error(" send sms 1023, 单个手机号 30 秒内下发短信条数超过设定的上限 telephone: " + telephone);
                    return ErrorCodeEnum.SMS_SEND_COUNT_LIMIT;
                case 1024:
                    log.error(" send sms 1024, 单个手机号 1 小时内下发短信条数超过设定的上限 telephone: " + telephone);
                    return ErrorCodeEnum.SMS_SEND_COUNT_LIMIT;
                case 1025:
                    log.error(" send sms 1025, 单个手机号日下发短信条数超过设定的上限 telephone: " + telephone);
                    return ErrorCodeEnum.SMS_SEND_COUNT_LIMIT;
                    default:
                        return ErrorCodeEnum.SMS_SEND_REST_ERROR;
            }
        }catch (HTTPException e){
            log.error("send sms failed!" + e);
            e.printStackTrace();
        }catch (JSONException e) {
            log.error("send sms failed!" + e);
            e.printStackTrace();
        }catch (IOException e){
            log.error("send sms failed!" + e);
            e.printStackTrace();
        }
        return ErrorCodeEnum.SMS_SEND_REST_ERROR;
    }


    @Override
    @Transactional(isolation= Isolation.READ_COMMITTED,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public ErrorCodeEnum check(String telephone, String code) {
        SmsQueue sms = smsQueueMapper.findByTelephone(telephone);
        int result = 0;
        if(null == sms){
            return ErrorCodeEnum.VALIDATE_CODE_INVALID;
        }
        if(StringUtils.isEmpty(code) || !sms.getCode().equals(code.trim())){
            return ErrorCodeEnum.VALIDATE_CODE_INVALID;
        }
        if(new Date().compareTo(sms.getExpiredTime()) > 0){
            result = smsQueueMapper.deleteByTelephone(telephone);
            if(result <= 0 ){
                return ErrorCodeEnum.SYSTEM_EXCEPTION;
            }
            return ErrorCodeEnum.VALIDATE_CODE_EXPIRED;
        }
        result = smsQueueMapper.deleteByTelephone(telephone);
        if(result <= 0 ){
            return ErrorCodeEnum.SYSTEM_EXCEPTION;
        }
        return ErrorCodeEnum.SUCCESS;
    }
}
