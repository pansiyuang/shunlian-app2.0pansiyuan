package com.shunlian.app.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daidingkang.SnapUpCountDownTimerView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.JosnSensorsDataAPI;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/3/26.
 */

public class DemoAct extends BaseActivity implements DayDayViewDemo {
    @BindView(R.id.rv_menu)
    RecyclerView rv_menu;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private DayDaypresenterDemo dayDayPresenter;
    private DayDayMenuAdapterDemo dayDayMenuAdapter;
    private int initPosition = 0;
    private DayListAdapterDemo dayListAdapter;
    private String id;
    private LinearLayoutManager linearLayoutManager;
    public static void startAct(Context context) {
        Intent intent = new Intent(context, DemoAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_demo_1;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        closeSideslip();
        minitData();
        nei_empty.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.day_haohuotaiduo));
        nei_empty.setButtonText(null);

    }

    public void initListener() {
        super.initListener();
        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (dayDayPresenter != null) {
                            dayDayPresenter.refreshBaby();
                        }
                    }
                }
            }
        });
    }

    public void minitData() {
        dayDayMenuAdapter = null;
        dayListAdapter = null;
        dayDayPresenter = new DayDaypresenterDemo(this, this);
        dayDayPresenter.initMenu("", 1, 20);

    }

    public void getApiData(final ActivityListEntity activityListEntity, int allPage, final int page, final List<ActivityListEntity.MData.Good.MList> list) {
        if (rv_menu == null) {
            return;
        }
        if (dayDayMenuAdapter == null) {
            dayDayMenuAdapter = new DayDayMenuAdapterDemo(this, false, activityListEntity.menu, initPosition);
            LinearLayoutManager newManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rv_menu.setLayoutManager(newManager);
            rv_menu.setAdapter(dayDayMenuAdapter);
            rv_menu.scrollToPosition(initPosition - 1);
            dayDayMenuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (dayDayMenuAdapter != null && rv_list.getScrollState() == 0) {
                        dayDayMenuAdapter.selectPosition = position;
                        dayDayMenuAdapter.notifyDataSetChanged();
                        dayListAdapter = null;
                        if (position < activityListEntity.menu.size())
                            id = activityListEntity.menu.get(position).id;
                        dayDayPresenter.resetBaby(id);
                    }
                }
            });
        } else {
            dayDayMenuAdapter.notifyDataSetChanged();
        }
        if (dayListAdapter == null) {
            dayListAdapter = new DayListAdapterDemo(this, true, list, dayDayPresenter, activityListEntity);
            dayListAdapter.id = id;
            linearLayoutManager = new LinearLayoutManager(this);
            rv_list.setLayoutManager(linearLayoutManager);
            rv_list.setAdapter(dayListAdapter);
            dayListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position < list.size() && !isEmpty(list) && list.get(position) != null) {
                        // activityListEntity.from;//踩点
                        JosnSensorsDataAPI.daydayGoodClick(activityListEntity.menu.get(dayDayMenuAdapter.selectPosition).time, list.get(position).goods_id, list.get(position).title, position);
                        GoodsDetailAct.startAct(DemoAct.this, list.get(position).goods_id);
                    }
                }
            });

        } else {
            dayListAdapter.title = activityListEntity.datas.title;
            dayListAdapter.isStart = activityListEntity.datas.sale;
            dayListAdapter.content = activityListEntity.datas.content;
            dayListAdapter.time = activityListEntity.datas.time;
            dayListAdapter.from = activityListEntity.from;
            dayListAdapter.notifyDataSetChanged();
        }
//        dayListAdapter.setPageLoading(page, allPage);


    }

    @Override
    public void activityState(int position) {
            dayListAdapter.notifyItemChanged(position);
    }

    @Override
    public void initMenu(List<ActivityListEntity.Menu> menus) {
        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).checked.equals("1")) {
                dayDayPresenter.initApiData(menus.get(i).id, 1, 20);
                initPosition = i;
                return;
            } else if (i >= menus.size() - 1) {
                dayDayPresenter.initApiData("", 1, 20);
            }


        }


    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
