package com.shunlian.app.utils;

/**
 * Created by Administrator on 2017/10/19.
 */

public class Code {

    public static final int STATUS_ERROR_TIMEOUT = 910;//请求超时
    public static final int STATUS_ERROR_PARAMS = 930; //参数错误
    public static final int STATUS_ERROR_PARAMS_MUST = 931; //参数丢失(同时列举需要的参数)
    public static final int STATUS_ERROR_PARAMS_CAN = 932; //参数超出范围(同时列举超出的参数)
    public static final int STATUS_ERROR_METHOD = 939; //不接受的请求类型
    public static final int STATUS_ERROR_API_VALIDE_SESSION = 998; //session失效
    public static final int STATUS_ERROR_API_VALIDE_TOKEN = 999; //token失效
    public static final int CODE_NO_LOGIN = 203;//未登录
    public static final int CODE_REFRESH_TOKEN_VALIDE = 204;//刷新token过期,让用户登录
}
