package com.shunlian.app.view;

import com.shunlian.app.bean.LoginFinishEntity;

/**
 * Created by zhanghe on 2018/7/23.
 */

public interface IRegisterAndBindView extends IView {

    /**
     * 图形验证码
     * @param bytes
     */
    default void setCode(byte[] bytes){}

    /**
     * 检验手机号是否正确
     * @param isRight true 正确 否则错误
     */
    default void iSMobileRight(boolean isRight){}

    /**
     * 推荐人是否正确
     * @param isRight true 正确 否则错误
     */
    default void isRefereesId(boolean isRight){}

    /**
     * 短信验证码
     * @param message
     */
    default void smsCode(String message){}

    /**
     * 手机号登录成功
     * @param entity
     */
    default void loginMobileSuccess(LoginFinishEntity entity){}

    /**
     * 找回密码成功
     * @param message
     */
    default void findPwdSuccess(String message){}

    /**
     * 检验手机短信验证码
     * @param message
     */
    default void checkMobileSmsCode(String message){}
}
