package com.shunlian.app.view;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IBalancePaySetTwo extends IView {
    void setPasswordCall();
    void changePasswordCall();
    void checkPasswordCall(boolean isRight,String key);
    void unbindAliPayCall(boolean isOk);
    void checkRuleValidCall(boolean isOk);
}
