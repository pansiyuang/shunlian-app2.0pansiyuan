package com.shunlian.app.view;

/**
 * Created by Administrator on 2017/10/23.
 */

public interface IRegisterOneView extends IView {

    void  setCode(byte[] bytes);

    void smsCode(String smsCode);

    /**
     * 推荐人id验证
     * @param isSuccess
     */
    void checkCode(boolean isSuccess);
}
