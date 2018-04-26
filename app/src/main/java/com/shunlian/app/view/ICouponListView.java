package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.CouponListEntity;

/**
 * Created by Administrator on 2018/4/19.
 */

public interface ICouponListView extends IView {
    /**
     * 设置适配器
     * @param adapter
     */
    void setAdapter(BaseRecyclerAdapter adapter);

    /**
     * 设置优惠券数量
     */
    void setCouponNum(CouponListEntity.NumInfo numInfo);
}
