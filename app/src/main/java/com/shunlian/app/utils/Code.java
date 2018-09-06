package com.shunlian.app.utils;

/**
 * Created by Administrator on 2017/10/19.
 */

public class Code {
    /**
     * 请求超时
     */
    public static final int STATUS_ERROR_TIMEOUT = 910;
    /**
     * 参数错误
     */
    public static final int STATUS_ERROR_PARAMS = 930;
    /**
     * 参数丢失(同时列举需要的参数)
     */
    public static final int STATUS_ERROR_PARAMS_MUST = 931;
    /**
     * 参数超出范围(同时列举超出的参数)
     */
    public static final int STATUS_ERROR_PARAMS_CAN = 932;
    /**
     * 不接受的请求类型
     */
    public static final int STATUS_ERROR_METHOD = 939;
    /**
     * session失效
     */
    public static final int STATUS_ERROR_API_VALIDE_SESSION = 998;
    /**
     * token失效
     */
    public static final int STATUS_ERROR_API_VALIDE_TOKEN = 999;
    /**
     * 未登录
     */
    public static final int CODE_NO_LOGIN = 203;//
    /**
     * 刷新token过期,让用户登录
     */
    public static final int CODE_REFRESH_TOKEN_VALIDE = 204;
    /**
     * 话费充值手机号错误
     */
    public static final int CREDIT_PHONE_ERROR = 2010;
    /**
     * 话费充值手机号查不到归属地
     */
    public static final int CREDIT_NO_BELONGING = 2011;
    /**
     * 话费充值手机号港澳台不支持
     */
    public static final int CREDIT_NO_SUPPORT = 2012;
    /**
     * 接口请求错误，但是不需要提示错误信息
     */
    public static final int API_ERROR_NO_MESSAGE = -4040;
}
