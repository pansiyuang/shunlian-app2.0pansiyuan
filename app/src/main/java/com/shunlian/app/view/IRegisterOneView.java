package com.shunlian.app.view;

/**
 * Created by Administrator on 2017/10/23.
 */

public interface IRegisterOneView extends IView {

    void  setCode(byte[] bytes);

    void smsCode(String smsCode);
}
