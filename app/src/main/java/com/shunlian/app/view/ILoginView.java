package com.shunlian.app.view;

import com.shunlian.app.bean.LoginFinishEntity;

/**
 * Created by Administrator on 2017/10/20.
 */

public interface ILoginView extends IView {
    void login(LoginFinishEntity content);

    void getSmsCode(String code);

    void loginFail(String erroMsg);
}
