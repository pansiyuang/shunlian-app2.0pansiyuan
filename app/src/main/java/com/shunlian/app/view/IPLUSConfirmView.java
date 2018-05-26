package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;

/**
 * Created by Administrator on 2017/11/29.
 */

public interface IPLUSConfirmView extends IView{
    /**
     * 商品总价
     * @param price
     */
    void goodsTotalPrice(String price,String addressId);

    void setAdapter(BaseRecyclerAdapter adapter);
}
