package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.PlusLogisticsAdapter;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.presenter.PlusLogisticsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IPlusLogisticsView;
import com.shunlian.app.widget.MyImageView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/28.
 */

public class PlusLogisticsDetailAct extends BaseActivity implements IPlusLogisticsView {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.tv_logistics_status)
    TextView tv_logistics_status;

    @BindView(R.id.tv_logistics_company)
    TextView tv_logistics_company;

    @BindView(R.id.tv_logistics_number)
    TextView tv_logistics_number;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private PlusLogisticsPresenter mPresenter;
    private PlusLogisticsAdapter traceAdapter;
    private String currentId,mType;

    public static void startAct(Context context, String query_id,String type) {
        Intent intent = new Intent(context, PlusLogisticsDetailAct.class);
        intent.putExtra("query_id", query_id);
        //type 类型 1是普通订单物流 2退换货物流用户 3退换货商家 4礼包
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_plus_logistics_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        currentId = getIntent().getStringExtra("query_id");
        mType = getIntent().getStringExtra("type");

        tv_title.setText(getStringResouce(R.string.logistics_info));
        mPresenter = new PlusLogisticsPresenter(this, this);
        mPresenter.getPlusLogistics(currentId,mType);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getLogistics(OrderLogisticsEntity logisticsEntity) {
        OrderLogisticsEntity.NowStatus nowStatus = logisticsEntity.now_status;

        GlideUtils.getInstance().loadImage(this, miv_icon, logisticsEntity.thumb);
        tv_logistics_company.setText("快递：" + logisticsEntity.express_com);
        tv_logistics_status.setText("物流状态：" + nowStatus.AcceptStation);
        tv_logistics_number.setText("单号：" + logisticsEntity.express_sn);

        if (!isEmpty(logisticsEntity.traces)) {
            List<OrderLogisticsEntity.Trace> traceList = logisticsEntity.traces;
            Collections.reverse(traceList);
            traceAdapter = new PlusLogisticsAdapter(this, traceList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recycler_list.setLayoutManager(linearLayoutManager);
            recycler_list.setNestedScrollingEnabled(false);
            recycler_list.setAdapter(traceAdapter);
        }
    }
}
