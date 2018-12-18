package com.fic.service.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Xie
 * @Date 2018/11/28
 * @Discription:
 **/
public class RegexUtil {

    private static Map<String, BigInteger> regexUnitMap;

    static{
        regexUnitMap = new HashMap<>();
        regexUnitMap.put("万",new BigInteger("10000"));
        regexUnitMap.put("十万",new BigInteger("100000"));
        regexUnitMap.put("百万",new BigInteger("1000000"));
        regexUnitMap.put("千万",new BigInteger("10000000"));
        regexUnitMap.put("亿",new BigInteger("100000000"));
    }

    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    public static boolean isPic(String fileName){
        Pattern pattern = Pattern.compile(".*(.png|.jpg|.bmp.|jpeg)$",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        if(matcher.matches()){
            return true;
        }
        return false;
    }

    public static int isVersion(String version){
        Pattern pattern = Pattern.compile("^[v][0-99]{1,2}[\\.][0-99]{1,2}[\\.][0-99]{1,2}",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(version);
        if(!matcher.matches())return 0;

        Pattern cutter = Pattern.compile("[^\\d]");
        Matcher cutterMatch = cutter.matcher(version);
        String versionCut = cutterMatch.replaceAll("");
        int versionCode = 0;

        try{
            versionCode = Integer.valueOf(versionCut);
            return versionCode;
        }catch (Exception e){
            e.printStackTrace();
            return versionCode;
        }

    }

    public static String replaceTelephone(String telephone){
        telephone = telephone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return telephone;
    };

    public static String ridChiese(String str){
        str = str.replaceAll("[\\u4e00-\\u9fa5]", "");
        return str;
    }

    public static String getChinese(String paramValue) {
        String regex = "([\u4e00-\u9fa5]+)";
        String str = "";
        Matcher matcher = Pattern.compile(regex).matcher(paramValue);
        while (matcher.find()) {
            str+= matcher.group(0);
        }
        return str;
    }

    /**
     *
     * @param unit 万，十万，百分，千万，亿
     * @param box 票房
     * @return
     */
    public static boolean checkIfOverChieseUnit(String unit, BigDecimal box){
        if(regexUnitMap.containsKey(unit)){
            BigInteger amountForTheUnit = regexUnitMap.get(unit);
            if(amountForTheUnit.compareTo(box.toBigInteger()) >= 0){
                return true;
            }
        }
        return false;
    }

    public static int matchOption(String choiceInput,BigDecimal box){
        String [] options = choiceInput.split(",");
//        for(String op: options){
//            BigDecimal
//        }
        return 0;
    }


    public static void main(String args[]){
        String version = "b1.12.1";
        System.out.println(RegexUtil.isVersion(version));
    }
}
