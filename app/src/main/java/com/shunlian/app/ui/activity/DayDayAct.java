package com.shunlian.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DayDayMenuAdapter;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.presenter.DayDayPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.DayDayView;

import butterknife.BindView;

public class DayDayAct extends BaseActivity implements View.OnClickListener, DayDayView{

    @BindView(R.id.rv_menu)
    RecyclerView rv_menu;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private boolean isFocus;
    private String storeId,seller_id,storeScore;

    private DayDayPresenter dayDayPresenter;
    private DayDayMenuAdapter dayDayMenuAdapter;

    public static void startAct(Context context, String storeId) {
        Intent intent = new Intent(context, DayDayAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_day_day;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.mrlayout_yingye:
//                StoreLicenseAct.startAct(this,seller_id);
//                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
//        mtv_attention.setOnClickListener(this);
//        mrlayout_yingye.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        storeId = getIntent().getStringExtra("storeId");
        dayDayPresenter = new DayDayPresenter(this, this);
        dayDayPresenter.initApiData();
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void getApiData(ActivityListEntity activityListEntity) {
        rv_menu.setVisibility(View.VISIBLE);
        if (dayDayMenuAdapter == null) {
            dayDayMenuAdapter = new DayDayMenuAdapter(this, false, activityListEntity.menu);
            LinearLayoutManager newManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rv_menu.setLayoutManager(newManager);
            rv_menu.setAdapter(dayDayMenuAdapter);
            dayDayMenuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (rv_list.getScrollState()==0){
                        dayDayMenuAdapter.selectPosition = position;
                        dayDayMenuAdapter.notifyDataSetChanged();

                    }
                }
            });
        } else {
            dayDayMenuAdapter.notifyDataSetChanged();
        }
    }
}
