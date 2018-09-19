package com.shunlian.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DayDayMenuAdapter;
import com.shunlian.app.adapter.DayListAdapter;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.DayDayPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.DayDayView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DayDayAct extends BaseActivity implements View.OnClickListener, DayDayView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.rv_menu)
    RecyclerView rv_menu;

    @BindView(R.id.mtv_msg_count)
    MyTextView mtv_msg_count;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private DayDayPresenter dayDayPresenter;
    private DayDayMenuAdapter dayDayMenuAdapter;
    private DayListAdapter dayListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String id;
    private int initPosition=0;
    private MessageCountManager messageCountManager;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, DayDayAct.class);
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

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.activity();
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        closeSideslip();
        minitData();
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
        nei_empty.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.day_haohuotaiduo));
        nei_empty.setButtonText(null);
    }

    @Override
    protected void onResume() {
        if (messageCountManager.isLoad()) {
            String s = messageCountManager.setTextCount(mtv_msg_count);
            if (quick_actions != null)
                quick_actions.setMessageCount(s);
        } else {
            messageCountManager.initData();
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(mtv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    public void minitData() {
        dayDayMenuAdapter = null;
        dayListAdapter = null;
        dayDayPresenter = new DayDayPresenter(this, this);
        dayDayPresenter.initMenu("", 1, 20);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {
        visible(nei_empty);
        gone(rv_list);
    }

    @Override
    public void getApiData(final ActivityListEntity activityListEntity, int allPage, final int page, final List<ActivityListEntity.MData.Good.MList> list) {
        if (dayDayMenuAdapter == null) {
            dayDayMenuAdapter = new DayDayMenuAdapter(this, false, activityListEntity.menu,initPosition);
            LinearLayoutManager newManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
            rv_menu.setLayoutManager(newManager);
            rv_menu.setAdapter(dayDayMenuAdapter);
            rv_menu.scrollToPosition(initPosition-1);
//            rv_menu.smoothScrollToPosition(initPosition-1);//无效
            dayDayMenuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (dayDayMenuAdapter != null && rv_list.getScrollState() == 0) {
                        dayDayMenuAdapter.selectPosition = position;
                        dayDayMenuAdapter.notifyDataSetChanged();
                        dayListAdapter = null;
                        if (position<activityListEntity.menu.size())
                        id = activityListEntity.menu.get(position).id;
                        dayDayPresenter.resetBaby(id);
                    }
                }
            });
        } else {
            dayDayMenuAdapter.notifyDataSetChanged();
        }

        if (isEmpty(list)) {
            visible(nei_empty);
            gone(rv_list);
        } else {
            gone(nei_empty);
            visible(rv_list);
        }
        if (dayListAdapter == null) {
            dayListAdapter = new DayListAdapter(this, true, list, dayDayPresenter, activityListEntity);
            dayListAdapter.id = id;
            linearLayoutManager = new LinearLayoutManager(this);
            rv_list.setLayoutManager(linearLayoutManager);
            rv_list.setAdapter(dayListAdapter);
            dayListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                    activityListEntity.from;//踩点
                    GoodsDetailAct.startAct(DayDayAct.this, list.get(position).goods_id);
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
        dayListAdapter.setPageLoading(page, allPage);
    }

    @Override
    public void activityState(int position) {
        dayListAdapter.notifyItemChanged(position);
    }

    @Override
    public void initMenu(List<ActivityListEntity.Menu> menus) {
        for (int i = 0; i < menus.size(); i++) {
            if ("1".equals(menus.get(i).checked)) {
//            if (i==3) {
                dayDayPresenter.initApiData(menus.get(i).id, 1, 20);
                initPosition=i;
                return;
            }else if (i>=menus.size()-1){
                dayDayPresenter.initApiData("", 1, 20);
            }
        }
    }


    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(mtv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
