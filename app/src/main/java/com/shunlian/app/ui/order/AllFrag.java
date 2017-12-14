package com.shunlian.app.ui.order;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.OrderListAdapter;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.ui.BaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AllFrag extends BaseFragment {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_order_list,container,false);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
    }

    public void setOrderList(List<MyOrderEntity.Orders> orders, int page, int allPage) {
        OrderListAdapter adapter = new OrderListAdapter(baseActivity,true,orders);
        recy_view.setAdapter(adapter);
    }
}
