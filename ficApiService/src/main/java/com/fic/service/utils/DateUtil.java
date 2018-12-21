package com.fic.service.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
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

    public static String formatMin(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String getYesTodayAndFormatDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusDays(1);
        String dateString = localDate.format(formatter);
        return dateString;
    }

    public static String getYestodayForMaoYan() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusDays(1);
        String dateString = localDate.format(formatter);
        return dateString;
    }

    public static Date plusDateOneDay(Date date,int dayNum) {
        LocalDate localDate = DateUtil.dateToLocalDate(date);
        localDate = localDate.plusDays(dayNum);
        date = DateUtil.LocalDateToDate(localDate);
        return date;
    }

    public static Date minDateOneDay(Date date) {
        LocalDate localDate = DateUtil.dateToLocalDate(date);
        localDate = localDate.minusDays(1);
        date = DateUtil.LocalDateToDate(localDate);
        return date;
    }

    public static String minDateNDay(Date date,int day) {
        LocalDate localDate = DateUtil.dateToLocalDate(date);
        localDate = localDate.minusDays(day);
        date = DateUtil.LocalDateToDate(localDate);
        return DateUtil.dateToStrMatDay(date);
    }

    public static String dateToStrMatDay(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
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

    public static Date toDayFormatDay(String dateStr){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date result = null;
        try{
            if(StringUtils.isNotEmpty(dateStr)){
                result = formatter.parse(dateStr);
                return result;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date toDayFormatDay_1(String dateStr){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date result = null;
        try{
            if(StringUtils.isNotEmpty(dateStr)){
                result = formatter.parse(dateStr);
                return result;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date toMinFormatDay(String dateStr){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date result = null;
        try{
            if(StringUtils.isNotEmpty(dateStr)){
                result = formatter.parse(dateStr);
                return result;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public static boolean betLockTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime startLock = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),23,50,00);
        LocalDateTime endLock = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),00,20,00);
        /** 开奖(23:50:00 - 00:20:00)，不允许下注*/
        if(date.compareTo(startLock) <0){
            return false;
        }
        if(date.compareTo(endLock) >0){
            return false;
        }
        //        if(date.compareTo(startLock) <=0){
//            return true;
//        }
//        if(date.compareTo(endLock) >=0){
//            return true;
//        }
        System.out.println("start lock time : " + formatter.format(startLock));
        System.out.println("end lock time : " + formatter.format(endLock));
        System.out.println("now time : " + formatter.format(date));
        return true;
    }


    public static void main(String args[]){
        DateUtil.betLockTime();
    }

}
