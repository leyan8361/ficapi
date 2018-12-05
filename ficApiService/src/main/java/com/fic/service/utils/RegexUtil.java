package com.fic.service.utils;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Xie
 * @Date 2018/11/28
 * @Discription:
 **/
public class RegexUtil {

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

    public static void main(String args[]){
        String version = "b1.12.1";
        System.out.println(RegexUtil.isVersion(version));
    }
}
