package com.fic.service.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:流水号生成工具
**/
public class SerialNumGenerateUtil {

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    public synchronized static String getSerialNumber() {
        String t = getStringDate();
        int x = (int) (Math.random() * 900) + 100;
        String serial = t + x;
        return serial;
    }
}