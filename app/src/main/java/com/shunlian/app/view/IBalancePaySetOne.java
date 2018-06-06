package com.shunlian.app.view;

import com.shunlian.app.bean.BalanceInfoEntity;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IBalancePaySetOne extends IView {
    void getCodeCall();
    void nextCall(String key);
    void bindAlipayCall(String account_number);
    void setApiData(BalanceInfoEntity data);
}
