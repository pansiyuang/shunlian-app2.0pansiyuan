package com.shunlian.app.view;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IBalancePaySetOne extends IView {
    void getCodeCall();
    void nextCall(String key);
    void bindAlipayCall(String account_number);
}
