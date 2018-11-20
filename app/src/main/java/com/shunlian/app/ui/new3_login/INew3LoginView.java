package com.shunlian.app.ui.new3_login;

import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.view.IView;

/**
 * Created by zhanghe on 2018/11/20.
 */

public interface INew3LoginView extends IView {
    /**
     * 设置图形验证码
     * @param bytes
     */
    default void setCode(byte[] bytes){}

    /**
     * 账号密码登录
     * @param data
     */
    default void accountPwdSuccess(LoginFinishEntity data){}

    /**
     * 手机号是否正确
     * @param b
     */
    default void iSMobileRight(boolean b,String msg){}

    /**
     * 短信验证码
     * @param message
     */
    default void smsCode(String message){}
}
