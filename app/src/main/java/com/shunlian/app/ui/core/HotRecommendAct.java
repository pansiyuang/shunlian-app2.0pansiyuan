package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HotPushAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PAishang;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.timer.DayRedWhiteDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.view.IAishang;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/7.
 */

public class HotRecommendAct extends BaseActivity implements View.OnClickListener, IAishang, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.miv_bg)
    MyImageView miv_bg;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    @BindView(R.id.msv_out)
    MyScrollView msv_out;

    @BindView(R.id.downTime_firsts)
    DayRedWhiteDownTimerView downTime_firsts;

    @BindView(R.id.miv_arrow)
    MyImageView miv_arrow;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;

    private PAishang pAishang;
    private HotPushAdapter hotPushAdapter;
    private String hotId;
    private boolean isMore=false;

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.channel();
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(getBaseContext());
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(tv_msg_count);
                quick_actions.setMessageCount(s);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_msg_count);
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
        if (downTime_firsts!=null){
            downTime_firsts.cancelDownTimer();
            downTime_firsts=null;
        }
        super.onDestroy();
    }

    public static void startAct(Context context,String hotId) {
        Intent intent = new Intent(context, HotRecommendAct.class);
        intent.putExtra("hotId",hotId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.miv_arrow:
                if (isMore){
                    miv_arrow.setImageResource(R.mipmap.icon_common_arrowdowns);
                    mtv_desc.setLines(2);
                    isMore=false;
                }else {
                    isMore=true;
                    mtv_desc.setLines(3);
                    miv_arrow.setImageResource(R.mipmap.icon_common_arrowups);
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_hot_recommend;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_arrow.setOnClickListener(this);
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom){
                    if (pAishang != null) {
                        pAishang.refreshBaby("push",hotId);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        hotId=getIntent().getStringExtra("hotId");
        pAishang = new PAishang(this, this);
        pAishang.getHotRd(hotId);
    }

    @Override
    public void setPushData(List<HotRdEntity.MData> mData, HotRdEntity data) {
        if (hotPushAdapter==null){
            mtv_title.setText(data.name);
            mtv_desc.setText(data.content);
            GlideUtils.getInstance().loadImage(getBaseContext(),miv_bg,data.pic);
            downTime_firsts.setDownTime(Integer.parseInt(data.count_down));
            downTime_firsts.setDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onFinish() {
                    downTime_firsts.cancelDownTimer();
                }

            });
            hotPushAdapter=new HotPushAdapter(getBaseContext(),mData);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 2);
            rv_list.setLayoutManager(gridLayoutManager);
            rv_list.setAdapter(hotPushAdapter);
            rv_list.setNestedScrollingEnabled(false);
            //在CoordinatorLayout中使用添加分割线失效
//            GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(TransformUtil.dip2px(getBaseContext(), 5), false);
//            rv_category.addItemDecoration(gridSpacingItemDecoration);
        }else {
            hotPushAdapter.notifyDataSetChanged();
        }
        hotPushAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.allPage));
    }
    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setNewData(CoreNewEntity coreNewEntity) {

    }

    @Override
    public void setHotData(CoreHotEntity coreHotEntity) {

    }

    @Override
    public void setHotsData(List<CoreHotEntity.Hot.Goods> mData, String page, String total) {

    }

    @Override
    public void setNewsData(List<CoreNewsEntity.Goods> mData, String page, String total) {

    }


    @Override
    public void setPingData(CorePingEntity corePingEntity) {
         }

}
