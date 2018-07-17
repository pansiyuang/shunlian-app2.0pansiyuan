package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;

/**
 * Created by zhanghe on 2018/7/13.
 */

public interface IMoreCreditView extends IView {

    void setAdapter(BaseRecyclerAdapter adapter);

    /**
     * 设置号码归属地
     * @param phone
     * @param card_address
     */
    void setBelongingTo(String phone,String card_address);

    /**
     * 手机号码错误
     */
    void phoneError();

    void setTopUpHistoryAdapter(BaseRecyclerAdapter adapter);
}
