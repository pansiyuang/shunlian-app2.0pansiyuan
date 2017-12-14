package com.shunlian.app.ui.confirm_order;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.TraceAdapter;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.presenter.OrderLogisticsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ITraceView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderLogisticsActivity extends BaseActivity implements ITraceView {

    @BindView(R.id.recycler_order_logistics)
    RecyclerView recycler_order_logistics;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.tv_title_number)
    TextView tv_title_number;

    public OrderLogisticsPresenter orderLogisticsPresenter;
    public TraceAdapter traceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_logistics;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        rl_title_more.setVisibility(View.VISIBLE);
        orderLogisticsPresenter = new OrderLogisticsPresenter(this, this);
        orderLogisticsPresenter.orderLogistics("170516Q63646267424");
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getLogistics(OrderLogisticsEntity logisticsEntity) {
        traceAdapter = new TraceAdapter(this, false, logisticsEntity.traces);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_order_logistics.setLayoutManager(linearLayoutManager);
        recycler_order_logistics.setAdapter(traceAdapter);
    }
}
