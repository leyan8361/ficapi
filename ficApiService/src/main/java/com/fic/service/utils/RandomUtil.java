package com.fic.service.utils;

import org.apache.commons.lang3.RandomStringUtils;

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

}
