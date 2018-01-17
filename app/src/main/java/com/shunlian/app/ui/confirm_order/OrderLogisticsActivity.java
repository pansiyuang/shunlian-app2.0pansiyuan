package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
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

    @BindView(R.id.tv_title)
    TextView tv_title;

    public OrderLogisticsPresenter orderLogisticsPresenter;
    public TraceAdapter traceAdapter;
    private String currentOrder;

    public static void startAct(Context context, String orderStr) {
        Intent intent = new Intent(context, OrderLogisticsActivity.class);
        intent.putExtra("order", orderStr);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_logistics;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        rl_title_more.setVisibility(View.VISIBLE);
        tv_title.setText(getStringResouce(R.string.logistics_detail));
        currentOrder = getIntent().getStringExtra("order");

        if (!isEmpty(currentOrder)) {
            orderLogisticsPresenter = new OrderLogisticsPresenter(this, this);
            orderLogisticsPresenter.orderLogistics(currentOrder);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getLogistics(OrderLogisticsEntity logisticsEntity) {
        if (logisticsEntity == null || logisticsEntity.traces == null) {
            return;
        }
        traceAdapter = new TraceAdapter(this, false, logisticsEntity.traces, logisticsEntity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_order_logistics.setLayoutManager(linearLayoutManager);
        recycler_order_logistics.setNestedScrollingEnabled(false);
        recycler_order_logistics.setAdapter(traceAdapter);
    }
}
