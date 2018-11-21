package com.shunlian.app.ui.new3_login;

import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
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
    default void smsCode(String state,String message){}

    /**
     * 检查短信验证码
     * @param msg
     * @param vcode_status
     */
    default void checkSmsCode(String msg,String vcode_status){}

    /**
     * 登录成功
     * @param data
     */
    default void loginMobileSuccess(LoginFinishEntity data){}

    /**
     * 邀请码详情
     * @param bean
     */
    default void codeInfo(MemberCodeListEntity bean){}
}
