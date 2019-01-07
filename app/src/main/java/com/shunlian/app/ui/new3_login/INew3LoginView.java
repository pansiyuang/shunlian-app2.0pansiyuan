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
     * @param msg
     * @param shareid 邀请码
     */
    default void iSMobileRight(boolean b,String msg,String shareid){}

    /**
     * 短信验证码
     * @param showPictureCode 1显示图像验证码
     * @param error 错误信息
     */
    default void smsCode(int showPictureCode,String error){}

    /**
     * 检查短信验证码
     * @param showPictureCode 1显示图像验证码
     * @param error 错误信息
     */
    default void checkSmsCode(int showPictureCode,String error){}

    /**
     * 登录成功
     * @param data
     */
    default void loginMobileSuccess(LoginFinishEntity data){}

    /**
     * 邀请码详情
     * @param bean
     */
    default void codeInfo(MemberCodeListEntity bean,String error){}

    /**
     * 登录信息
     * @param data
     */
    default void setLoginInfoTip(New3LoginInfoTipEntity data){}

    /**
     * 检验图形验证码
     * @param message
     */
    default void checkPictureCode(String message){}

    /**
     * 从微信界面过来绑定手机号验证
     * @param status
     * @param msg
     */
    default void checkFromWXMobile(String status,String msg){}

    /**
     * 绑定上级提示
     * @param tip
     */
    default void bindShareID(String tip){}
}
