package com.hnxjgou.xinjia;

/**
 * Created by apple on 2018/5/28.
 */

public class ApiConfig {

    /**用户相关API*/
    public static final class user {
        /**登录-API名称*/
        public final static String API_LOGIN = "Login";

        /**注册-API名称*/
        public final static String API_REGISTER = "register";

        /**获取验证码-API名称*/
        public final static String API_CODE = "sendMessage";

        private static final String BASE_PROJECT_URL = "http://www.hnxjgou.com/wechat/api/user/userApi.asmx/";

        public static String build_url(String api_method){
            return BASE_PROJECT_URL + api_method;
        }
    }

    /**类别相关API*/
    public final static class category{

        /**获取类别列表-API名称*/
        public final static String API_CATEGORY = "category";

        private static final String BASE_PROJECT_URL = "http://www.hnxjgou.com/wechat/api/category/categoryApi.asmx/";

        public static String build_url(String api_method){
            return BASE_PROJECT_URL + api_method;
        }
    }
}