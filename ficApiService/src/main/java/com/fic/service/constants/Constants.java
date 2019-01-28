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

        public static final Long TOKEN_VALIDITY_DAYS = 60l; //token 30 days

        public static final int DEFAULT_TOKEN_LENGTH = 16;

        public static final int KEEP_SCALE = 8;

        /**默认nick name*/
        public static final String NICK_NAME = "新手报道";

        /**默认头像*/
        public static final String DEFAULT_HEAD_PATH = "/user/default.png";

        /**金句分割符*/
        public static final String WORDS_CUT = "\\|";
        /**
         * 文件类型
         */
        public static final String MEDIA_PREFIX_ACTOR="movie/media/actor/";
        public static final String MEDIA_PREFIX_BRIEF="movie/media/brief/";
        public static final String MEDIA_PREFIX_PLOT="movie/media/plot/";
        public static final String MOVIE_COVER="movie/cover/";
        public static final String BOOT_PAGE_COVER="bootpage/";
        public static final String LUCK_COVER_PATH="lucky";
        public static final String HEAD_CUT_PATH="user/";
        public static final String USER_AUTH_PATH="user/auth/";
        public static final String USER_AUTH_FRONT_PATH="front/";
        public static final String USER_AUTH_BACK_PATH="back/";
        public static final String BET_MOVIE_COVER_PATH="/bet/movie/";
        public static final String BANNER_URL_PATH ="banner/";
        public static final String APP_FILE_PATH="app/";

        /**
         * 币种
         */
        public static final String FIC = "FIC";
        public static final String CNY = "CNY";
        public static final String ETH = "ETH";
        public static final String BTC = "BTC";
        public static final String BCH = "BCH";
        public static final String USDT = "USDT";
        public static final String TFC = "TFC";
        /**
         * 连续竞猜标记
         */
        public static final String FIVE_T = "fiveT";
        public static final String TWO_T = "twoT";
        public static final String THREE_T = "threeT";
        public static final String FOUR_T = "fourT";
        public static final String FIVE_PERCENT = "0.4";
        public static final String TWO_PERCENT = "0.1";
        public static final String THREE_PERCENT = "0.2";
        public static final String FOUR_PERCENT = "0.3";

        public static final String LOGIN_PATH = "/api/v1/login";
        public static final String REGISTER_PATH = "/api/v1/register";
        public static final String RESET_PASSWORD_PATH = "/api/v1/resetPassword";
        public static final String RESOURCE_PATH = "/api/v1/static/**";
        public static final String SMS_PATH = "/api/v1/sendSms";
        public static final String EMAIL_PATH = "/api/v1/sendEmail";
        public static final String CHECK_CODE_PATH = "/api/v1/checkCode";//验证码
        public static final String VERSION_CHECK_PATH = "/api/v1/checkVersion";//版本号
        public static final String BANNER_PATH = "/api/v1/banner/**";//版本号
        public static final String LOG_OUT_PATH = "/api/v1/logout/**";//登出
        public static final String BOOT_PAGE_PATH = "/api/v1/bootPage/**";//登出
        public static final String WE_CHAT_NOTIFY_PATH = "/api/v1/weChat/notify";//微信异步回调
        public static final Map<String,String> pathMatchFilterMap = new HashMap<String,String>();

        static {
                pathMatchFilterMap.put(LOGIN_PATH,LOGIN_PATH);
                pathMatchFilterMap.put(REGISTER_PATH,REGISTER_PATH);
                pathMatchFilterMap.put(RESET_PASSWORD_PATH,RESET_PASSWORD_PATH);
                pathMatchFilterMap.put(RESOURCE_PATH,RESOURCE_PATH);
                pathMatchFilterMap.put(SMS_PATH,SMS_PATH);
                pathMatchFilterMap.put(CHECK_CODE_PATH,CHECK_CODE_PATH);
                pathMatchFilterMap.put(VERSION_CHECK_PATH,VERSION_CHECK_PATH);
                pathMatchFilterMap.put(BANNER_PATH,BANNER_PATH);
                pathMatchFilterMap.put(LOG_OUT_PATH,LOG_OUT_PATH);
                pathMatchFilterMap.put(BOOT_PAGE_PATH,BOOT_PAGE_PATH);
                pathMatchFilterMap.put(EMAIL_PATH,EMAIL_PATH);
                pathMatchFilterMap.put(WE_CHAT_NOTIFY_PATH,WE_CHAT_NOTIFY_PATH);
        }
}
