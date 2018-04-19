package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;

/**
 * Created by Administrator on 2018/4/13.
 */

public interface ISaleDetailView extends IView {


    void setAdapter(BaseRecyclerAdapter adapter);

    /**
     * 设置总销售 总收益
     * @param sale
     * @param profit
     */
    void setTotalSale_Profit(String sale,String profit);
}
