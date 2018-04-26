package com.shunlian.app.view;

/**
 * Created by Administrator on 2018/4/24.
 */

public interface IChangeUserView extends IView {
    /**
     * 设置图形验证码
     * @param bytes
     */
    default void setCode(byte[] bytes){}

    /**
     * 设置手机号
     * @param mobile
     */
    default void setMobile(String mobile){}

    /**
     * 验证成功
     */
    default void setCheckSuccess(){}

    /**
     * 验证用户身份的key
     * @param key
     */
    default void key(String key){}

    /**
     * 去绑定新手机
     * @param mobile
     * @param key
     */
    default void goBindNewMobile(String mobile,String key){}

    /**
     * 绑定手机号成功
     */
    default void bindMobileSuccess(){}

    /**
     * 修改密码成功
     */
    default void modifyPwdSuccess(){}

    /**
     * 修改key
     * @param key
     */
    default void modifyKey(String key){}
}
