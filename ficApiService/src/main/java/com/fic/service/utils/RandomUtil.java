package com.fic.service.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public final class RandomUtil {

    private static final int SMS_CODE_LENGTH = 6;

    private RandomUtil() {
    }

    public static String generateSmsCode(){
        String code = RandomStringUtils.randomNumeric(SMS_CODE_LENGTH);
        return code;
    }

    /**
     *
     * @return 指定长度以内的随机数
     */
    public static int getRandomNum(int length){
        int result = new Random().nextInt(length);
        return result;
    }

    public static void main(String args[]){
//        for(int i = 0 ; i < 100; i++){
//            System.out.println("第"+i+"次 : " + RandomUtil.getRandomNum());
//        }
        System.out.println(RandomUtil.getRandomNum(2));
    }

}
