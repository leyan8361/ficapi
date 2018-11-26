package com.fic.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author Xie
 * @Date 2018/11/26
 * @Discription:
 **/
public class DateUtil {

    public static LocalDate dateToLocalDate(Date newDate) throws ParseException {
        Instant instant = newDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }
}
