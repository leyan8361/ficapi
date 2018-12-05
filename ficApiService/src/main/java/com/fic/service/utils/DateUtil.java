package com.fic.service.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Author Xie
 * @Date 2018/11/26
 * @Discription:
 **/
public class DateUtil {

    public static LocalDate dateToLocalDate(Date newDate) {
        Instant instant = newDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }

    public static Date LocalDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static String formatSec(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static Date plusMin(Date date, int min) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MINUTE,min);
        date=calendar.getTime();
        return date;
    }

    public static String getTimeStamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String dateString = formatter.format(now);
        return dateString;
    }

    public static boolean isToday(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        String todayStr = formatter.format(today);
        String dateStr = formatter.format(date);
        if(todayStr.equals(dateStr)){
            return true;
        }
        return false;
    }

}
