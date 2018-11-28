package com.fic.service.scheduled;

import com.fic.service.mapper.SmsQueueMapper;
import com.fic.service.service.impl.SmsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @Author Xie
 * @Date 2018/11/28
 * @Discription:
 **/
@Service
public class ScheduledService {

    private final Logger log = LoggerFactory.getLogger(ScheduledService.class);

    @Autowired
    SmsQueueMapper smsQueueMapper;

    @Scheduled(cron = "0 0 0 * * *?")
    public void doSmsCleanUp() {
        log.debug(" delete sms queue action !");
        smsQueueMapper.deleteAllBeforeNow();
    }

}
