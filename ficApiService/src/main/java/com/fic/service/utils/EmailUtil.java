package com.fic.service.utils;

import com.fic.service.controller.api.v2.ApiV2MoneyController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@Component
public class EmailUtil {

    private final Logger log = LoggerFactory.getLogger(EmailUtil.class);

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String Sender;

    public void send(String receiver,String content){
        log.debug("发送邮件 receiver :{},content :{}",receiver,content);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Sender);
        message.setTo(receiver);
        message.setSubject("淘影实业");
        message.setText(content);
        mailSender.send(message);
    }
}
