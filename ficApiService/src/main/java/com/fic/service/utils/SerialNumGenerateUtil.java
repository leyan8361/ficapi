package com.fic.service.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *   @Author Xie
 *   @Date 2018/11/27
 *   @Discription:流水号生成工具
**/
public class SerialNumGenerateUtil {

    private static int addPart = 1;
    private static String result = "";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static String lastDate = "";
    private static int length = 20;

    /**
     * @return 返回17位时间戳+3位递增数
     */
    public synchronized static String getSerialNumber() {
        Date now = new Date();
        String nowStr = sdf.format(now);

        if (SerialNumGenerateUtil.lastDate.equals(nowStr)) {
            addPart += 1;
        } else {
            addPart = 1;
            lastDate = nowStr;
        }

        if (length > 17) {
            length -= 17;
            for (int i = 0; i < length - ((addPart + "").length()); i++) {
                nowStr += "0";
            }
            nowStr += addPart;
            result = nowStr;
        } else {
            result = nowStr;
        }
        return result;
    }

}
