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

        public static final String LOGIN_PATH = "/api/v1/login";
        public static final String REGISTER_PATH = "/api/v1/register";
        public static final String RESET_PASSWORD_PATH = "/api/v1/resetPassword";

        public static final Map<String,String> pathMatchFilterMap = new HashMap<String,String>();

        static {
                pathMatchFilterMap.put(LOGIN_PATH,LOGIN_PATH);
                pathMatchFilterMap.put(REGISTER_PATH,REGISTER_PATH);
                pathMatchFilterMap.put(RESET_PASSWORD_PATH,RESET_PASSWORD_PATH);
        }
}
