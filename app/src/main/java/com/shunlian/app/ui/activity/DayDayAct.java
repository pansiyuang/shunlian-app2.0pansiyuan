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
import com.shunlian.app.view.DayDayView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class DayDayAct extends BaseActivity implements View.OnClickListener, DayDayView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.rv_menu)
    RecyclerView rv_menu;

    @BindView(R.id.mtv_msg_count)
    MyTextView mtv_msg_count;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private DayDayPresenter dayDayPresenter;
    private DayDayMenuAdapter dayDayMenuAdapter;
    private DayListAdapter dayListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String id;
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

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        closeSideslip();
        minitData();
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
    }

    @Override
    protected void onResume() {
        if(messageCountManager.isLoad()){
            messageCountManager.setTextCount(mtv_msg_count);
        }else{
            messageCountManager.initData();
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(mtv_msg_count);
    }

    public void minitData(){
        dayDayMenuAdapter=null;
        dayListAdapter=null;
        dayDayPresenter = new DayDayPresenter(this, this);
        dayDayPresenter.initApiData("",1,20);
    }
    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void getApiData(final ActivityListEntity activityListEntity, int allPage, final int page, final List<ActivityListEntity.MData.Good.MList> list) {
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
                        dayListAdapter=null;
                        id=activityListEntity.menu.get(position).id;
                        dayDayPresenter.resetBaby(id);
                    }
                }
            });
        } else {
            dayDayMenuAdapter.notifyDataSetChanged();
        }
        if (dayListAdapter == null) {
            dayListAdapter = new DayListAdapter(this, true, list,dayDayPresenter, activityListEntity);
            dayListAdapter.id=id;
            linearLayoutManager = new LinearLayoutManager(this);
            rv_list.setLayoutManager(linearLayoutManager);
            rv_list.setAdapter(dayListAdapter);
            dayListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                    activityListEntity.from;//踩点
                    GoodsDetailAct.startAct(DayDayAct.this,list.get(position).goods_id);
                }
            });
        } else {
            dayListAdapter.title=activityListEntity.datas.title;
            dayListAdapter.isStart=activityListEntity.datas.sale;
            dayListAdapter.content=activityListEntity.datas.content;
            dayListAdapter.time=activityListEntity.datas.time;
            dayListAdapter.from=activityListEntity.from;
            dayListAdapter.notifyDataSetChanged();
        }
        dayListAdapter.setPageLoading(page, allPage);
    }

    @Override
    public void activityState(int position) {
        dayListAdapter.notifyItemChanged(position);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(mtv_msg_count);
    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
