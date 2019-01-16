package com.fic.service.utils;

import com.fic.service.Enum.PriceEnum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
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

    private static Map<String, BigDecimal> regexUnitMap;

    static{
        regexUnitMap = new HashMap<>();
        regexUnitMap.put("万",new BigDecimal("10000"));
        regexUnitMap.put("十万",new BigDecimal("100000"));
        regexUnitMap.put("百万",new BigDecimal("1000000"));
        regexUnitMap.put("千万",new BigDecimal("10000000"));
        regexUnitMap.put("亿",new BigDecimal("100000000"));
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

    public static boolean isEmail(String email) {
        String regex = "\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isMatch = m.matches();
        return isMatch;
    }
    public static boolean isPic(String fileName){
        Pattern pattern = Pattern.compile(".*(.png|.jpg|.bmp.|jpeg)$",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        if(matcher.matches()){
            return true;
        }
        return false;
    }

    public static boolean isApkOrIpa(String fileName){
        Pattern pattern = Pattern.compile(".*(.ipa|.apk)$",Pattern.CASE_INSENSITIVE);
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
     * @param unit 万，十万，百万，千万，亿
     * @param box 票房
     * @return
     */
    public static boolean checkIfOverChieseUnit(String unit, BigDecimal box){
        if(regexUnitMap.containsKey(unit)){
            BigDecimal amountForTheUnit = regexUnitMap.get(unit);
            box = box.multiply(new BigDecimal("10000"));
            if(box.compareTo(amountForTheUnit) > 0){
                return true;
            }
        }
        return false;
    }

    public static int matchOption(String choiceInput,BigDecimal box){
        String [] options = choiceInput.split(",");
        int result = 0;
        try{
            BigDecimal fiOp = new BigDecimal(options[0]);
            BigDecimal seOp = new BigDecimal(options[1]);
            BigDecimal thOp = new BigDecimal(options[2]);
            /** A 选项 box < fiOp */
            if(fiOp.compareTo(box) < 0){
                return PriceEnum.A_CHOICE.getCode();
            }
            /** B选项 fiOp <= box < seOp */
            if(fiOp.compareTo(box) >= 0 && seOp.compareTo(box) < 0){
                return PriceEnum.B_CHOICE.getCode();
            }
            /** C选项 seOp <= box < thOp */
            if(seOp.compareTo(box) >=0 && thOp.compareTo(box) < 0){
                return PriceEnum.C_CHOICE.getCode();
            }
            /** D选项 box > thOp */
            if(thOp.compareTo(box) >0 ){
                return PriceEnum.D_CHOICE.getCode();
            }
        }catch (Exception e){
            System.out.println(" 转换 中级场 选项 错误  e : "+e);
            return 0;
        }
        return 0;
    }

    public static String getLastNum(BigDecimal boxInfo){
        boxInfo = boxInfo.setScale(0,BigDecimal.ROUND_DOWN);
        String boxInfoStr = boxInfo.toString();
        String lastNum = boxInfoStr.substring(boxInfoStr.length()-1,boxInfoStr.length());
        System.out.println(lastNum);
        return lastNum;
    }

    public static boolean isCoinType(String coinType){
        Pattern pattern = Pattern.compile(".*(FTC|TFC|BTC|ETH|USDT|BCH|FIAT)$");
        Matcher matcher = pattern.matcher(coinType);
        if(matcher.matches()){
            return true;
        }
        return false;
    }


    public static void main(String args[]){
        System.out.println(RegexUtil.isEmail("11@163.com"));
    }
}
