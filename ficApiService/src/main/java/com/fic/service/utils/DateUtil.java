package com.fic.service.utils;

import com.fic.service.Enum.PriceEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Local;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.logging.SimpleFormatter;

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

    public static LocalDateTime dateToLocalDateTime(Date newDate) {
        Instant instant = newDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
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


    public static String dateToStrMatSec(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public static String startDay(Date date,int day){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        localDate = localDate.minusDays(day);
        localDate = localDate.withHour(0).withMinute(0).withSecond(01);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getToDayStart(){
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        localDate = localDate.withHour(0).withMinute(0).withSecond(02);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getThisWeekMonDay(){
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,4);
        int day = localDate.get(weekFields.dayOfWeek());
        if (day != 0) {
            day = day -1;
        }
        localDate = localDate.minusDays(day).withHour(0).withMinute(0).withSecond(01);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getLastSevenDayStart(){
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        localDate = localDate.minusDays(7).withHour(0).withMinute(0).withSecond(01);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getTodayDayEnd(){
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        localDate = localDate.withHour(23).withMinute(59).withSecond(58);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getThisWeekMonDayBegin(Date date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,4);
        int day = localDate.get(weekFields.dayOfWeek());
        if (day != 0) {
            day = day -1;
        }
        localDate = localDate.minusDays(day).withHour(0).withMinute(0).withSecond(01);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getThisWeekMonDayEnd(Date date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,4);
        int day = localDate.get(weekFields.dayOfWeek());
        if (day != 0) {
            day = day -1;
        }
        localDate = localDate.minusDays(day).withHour(23).withMinute(59).withSecond(59);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getThisMonthBegin(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM");
        LocalDateTime localDate = null;
        try {
            Date date_  = simpleFormatter.parse(date);
            localDate = dateToLocalDateTime(date_);
        }catch (ParseException e){
            e.printStackTrace();
        }
        localDate = localDate.withDayOfMonth(1).withHour(00).withMinute(00).withSecond(01);
        String result = formatter.format(localDate);
        return result;
    }
    public static String getThisMonthEnd(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM");
        LocalDateTime localDate = null;
        try {
            Date date_  = simpleFormatter.parse(date);
            localDate = dateToLocalDateTime(date_);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,localDate.getYear());
        calendar.set(Calendar.MONTH,localDate.getMonthValue()-1);
//        calendar.set(Calendar.DAY_OF_MONTH,1);
//        calendar.set(Calendar.HOUR,00);
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        localDate = localDate.withDayOfMonth(lastDayOfMonth).withHour(23).withMinute(59).withSecond(58);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getLastWeekMonDay(){
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        localDate = localDate.minusWeeks(1);
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,4);
        int day = localDate.get(weekFields.dayOfWeek());
        if (day != 0) {
            day = day -1;
        }
        localDate = localDate.minusDays(day).withHour(0).withMinute(0).withSecond(01);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getThisWeekSunDay(){
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        int nowDay = localDate.getDayOfWeek().getValue();
        int day = 7;
        if (day != 0) {
            day = day - nowDay;
        }
        localDate = localDate.plusDays(day).withHour(23).withMinute(59).withSecond(30);
        String result = formatter.format(localDate);
        return result;
    }

    public static String getLastWeekSunDay(){
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY,4);
        int day = localDate.get(weekFields.dayOfWeek());
        if (day != 0) {
            day = day -1;
        }
        localDate = localDate.minusDays(day).withHour(23).withMinute(59).withSecond(30);
        String result = formatter.format(localDate);
        return result;
    }


    public static String endDay(Date date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        localDate = localDate.withHour(23).withMinute(59).withSecond(59);
        String result = formatter.format(localDate);
        return result;
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

    public static Date toSecFormatDay(String dateStr){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public static int getDayOfMonth(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        localDate.getDayOfMonth();
        return localDate.getDayOfMonth();
    }

    public static boolean betLockTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.now();
//        LocalDateTime zeroTime = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),00,00,00);
//        LocalDateTime endLock= LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),00,20,00);
//        LocalDateTime startLock  = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),23,50,00);
        /** 开奖(23:50:00 - 00:20:00)，不允许下注*/
//        if(date.compareTo(zeroTime) >=0 && date.compareTo(endLock) <= 0){
//            /** 00:00:00 <= bet Time < 00:20:00*/
//            return true;
//        }
//        if(date.compareTo(startLock) >=0 && date.compareTo(zeroTime) >= 0){
//            /** 23:50:00 <= bet Time < 00:00:00*/
//            return true;
//        }
        /** 鉴于数据准确性 延迟开奖 */
        /** 开奖(2:50:00 - 3:20:00)，不允许下注*/
        LocalDateTime startLock  = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),2,50,00);
        LocalDateTime endLock  = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),3,20,00);
        if(date.compareTo(startLock) >=0 && date.compareTo(endLock) <=0){
            return true;
        }
//        System.out.println("start lock time : " + formatter.format(startLock));
//        System.out.println("end lock time : " + formatter.format(endLock));
//        System.out.println("now time : " + formatter.format(date));
        return false;
    }

    public static String getTheMaxStartDay(){
        return "1970-01-01 01:00:00";
    }
    public static String getTheMaxEndDay(){
        return "2500-12-31 23:59:00";
    }

    public static int getSubstractDay(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)
                {
                    timeDistance += 366;
                }
                else
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else
        {
            return day2-day1;
        }
    }

    public static String getUnixTime() {
        return Instant.now().toString();
    }

    public static int getWeekDay(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        int nowDay = localDate.getDayOfWeek().getValue();
        return nowDay;
    }

    public static void main(String args[]){
//        boxInfo.setScale(0,BigDecimal.ROUND_DOWN).remainder(new BigDecimal("2")).compareTo(BigDecimal.ZERO) == 0
//        BigDecimal test = new BigDecimal("7");
//        BigDecimal test1 = new BigDecimal("9");
//        System.out.println(test.divide(test1,2,BigDecimal.ROUND_DOWN));
    }



}
