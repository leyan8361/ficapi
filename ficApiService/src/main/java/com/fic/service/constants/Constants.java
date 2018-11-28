package com.fic.service.constants;

import java.util.HashMap;
import java.util.Map;

/**
 *   @Author Xie
 *   @Date 2018/11/23
 *   @Discription:常量
**/
public class Constants {

        public static final String TOKEN_KEY = "x-auth-token";

        public static final String UID = "userId";

        public static final Long TOKEN_VALIDITY_DAYS = 30l; //token 30 days

        public static final int DEFAULT_TOKEN_LENGTH = 16;

        public static final int KEEP_SCALE = 8;

        /**
         * 币种
         */
        public static final String FIC = "FIC";
        public static final String CNY = "CNY";
        public static final String ETH = "ETH";
        public static final String BTC = "BTC";

        public static final String LOGIN_PATH = "/api/v1/login";
        public static final String REGISTER_PATH = "/api/v1/register";
        public static final String RESET_PASSWORD_PATH = "/api/v1/resetPassword";
        public static final String RESOURCE_PATH = "/api/v1/static/**";
        public static final String SMS_PATH = "/api/v1/sendSms";
        public static final String CHECK_CODE = "/api/v1/checkCode";
        public static final Map<String,String> pathMatchFilterMap = new HashMap<String,String>();

        static {
                pathMatchFilterMap.put(LOGIN_PATH,LOGIN_PATH);
                pathMatchFilterMap.put(REGISTER_PATH,REGISTER_PATH);
                pathMatchFilterMap.put(RESET_PASSWORD_PATH,RESET_PASSWORD_PATH);
                pathMatchFilterMap.put(RESOURCE_PATH,RESOURCE_PATH);
                pathMatchFilterMap.put(SMS_PATH,SMS_PATH);
                pathMatchFilterMap.put(CHECK_CODE,CHECK_CODE);
        }
}
