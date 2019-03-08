package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.PingListAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PPingList;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.JosnSensorsDataAPI;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.timer.DayRedBlackDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.view.IPingList;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.popmenu.PopMenu;
import com.shunlian.app.widget.popmenu.PopMenuItem;
import com.shunlian.app.widget.popmenu.PopMenuItemCallback;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/7.
 */

public class PingpaiListAct extends BaseActivity implements View.OnClickListener, IPingList, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    @BindView(R.id.view_bg)
    View view_bg;

    @BindView(R.id.miv_arrow)
    MyImageView miv_arrow;

    @BindView(R.id.miv_dot)
    MyImageView miv_dot;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.miv_avar)
    MyImageView miv_avar;

    @BindView(R.id.miv_photo)
    MyImageView miv_photo;

    @BindView(R.id.miv_share)
    MyImageView miv_share;

    @BindView(R.id.downTime_firsts)
    DayRedBlackDownTimerView downTime_firsts;

    @BindView(R.id.msv_out)
    MyScrollView msv_out;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;
    private String currentId;
    private PPingList pPingList;
    private PingListAdapter pingListAdapter;
    private boolean isMore=false;
    private ShareInfoParam mShareInfoParam=new ShareInfoParam();
    private CorePingEntity.Share share;

    private ShareGoodDialogUtil shareGoodDialogUtil;
    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.channel();
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(baseAct);
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

    public static void startAct(Context context, String id) {
        Intent intent = new Intent(context, PingpaiListAct.class);
        intent.putExtra("id", id);
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
            case R.id.miv_share:
                if (!Common.isAlreadyLogin()) {
                    Common.goGoGo(this,"login");
                    return;
                }

                if (isEmpty(mShareInfoParam.shareLink) && pPingList != null) {
                    pPingList.getShareInfo(pPingList.sale, currentId);
                } else {
                    mShareInfoParam.title = share.title;
                    mShareInfoParam.desc = share.content;
                    mShareInfoParam.img = share.logo;
                    mShareInfoParam.shareLink = share.share_url;
                    mShareInfoParam.isSpecial = true;
                    shareGoodDialogUtil.shareGoodDialog(mShareInfoParam,false,false);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginRefresh(DefMessageEvent event){
        if (event.loginSuccess && pPingList != null){
            pPingList.getShareInfo(pPingList.sale,currentId);
        }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        mShareInfoParam.desc = baseEntity.data.desc;
        mShareInfoParam.img = baseEntity.data.userAvatar;
        mShareInfoParam.shareLink = baseEntity.data.shareLink;
        mShareInfoParam.title = baseEntity.data.title;
        mShareInfoParam.isSpecial = true;
        shareGoodDialogUtil.shareGoodDialog(mShareInfoParam,false,false);
//        shareStyle2Dialog();
    }

    /**
     * 分享微信和复制链接
     */
    public void shareStyle2Dialog(){
        PopMenu mPopMenu = new PopMenu.Builder().attachToActivity(this)
                .addMenuItem(new PopMenuItem("微信", getResources().getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem("复制链接", getResources().getDrawable(R.mipmap.icon_lianjie)))
                .setOnItemClickListener(new PopMenuItemCallback() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                WXEntryActivity.startAct(PingpaiListAct.this,
                                        "shareFriend", mShareInfoParam,0);
                                break;
                            case 1:
                                Common.copyText(PingpaiListAct.this,mShareInfoParam.shareLink,mShareInfoParam.desc,true);
                                break;
                        }
                    }
                }).build();
        if (!mPopMenu.isShowing()) {
            mPopMenu.show();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_ping_list;
    }

    @Override
    protected void initListener() {
        super.initListener();
        shareGoodDialogUtil = new ShareGoodDialogUtil(this);
        miv_arrow.setOnClickListener(this);
        miv_share.setOnClickListener(this);
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom && pPingList != null) {
                    pPingList.refreshBaby();
                }
                float alpha = ((float) y) / 250;
                if (y > 250) {
                    mtv_title.setAlpha(1);
                    view_bg.setAlpha(1);
                } else if (y > 150) {
                    mtv_title.setTextColor(getColorResouce(R.color.new_text));
                    view_bg.setAlpha(alpha);
                    mtv_title.setAlpha(alpha);
                    miv_close.setImageResource(R.mipmap.icon_common_back_black);
                    miv_share.setImageResource(R.mipmap.icon_home_share);
                    miv_dot.setImageResource(R.mipmap.icon_common_more_black);
                    miv_close.setAlpha(alpha);
                    miv_share.setAlpha(alpha);
                    miv_dot.setAlpha(alpha);
                    ImmersionBar.with(PingpaiListAct.this)
                            .statusBarDarkFont(true, 0.2f)
                            .init();
                } else if (y > 0) {
                    ImmersionBar.with(PingpaiListAct.this)
                            .statusBarDarkFont(true, 0.0f)
                            .init();
                    mtv_title.setTextColor(getColorResouce(R.color.white));
                    miv_share.setImageResource(R.mipmap.icon_home_share_b);
                    view_bg.setAlpha(0);
                    mtv_title.setAlpha(1);
                    miv_close.setAlpha(1 - alpha);
                    miv_share.setAlpha(1 - alpha);
                    miv_dot.setAlpha(1 - alpha);
                    miv_close.setImageResource(R.mipmap.icon_common_back_white);
                    miv_dot.setImageResource(R.mipmap.icon_common_more_white);
                }
            }
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        currentId = getIntent().getStringExtra("id");
        pPingList = new PPingList(this, this, currentId);
//        pPingList = new PPingList(this, this, "1");
        pPingList.getApiData();
        nei_empty.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.first_shangping));
        nei_empty.setButtonText(null);
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
    public void setApiData(CorePingEntity corePingEntity, List<CorePingEntity.MData> mDatas) {
        share=corePingEntity.share;
        if (isEmpty(mDatas)) {
            visible(nei_empty);
            gone(rv_list);
        } else {
            gone(nei_empty);
            visible(rv_list);
        }
        if (pingListAdapter==null){
            downTime_firsts.setDownTime(Integer.parseInt(corePingEntity.brand.count_down));
            downTime_firsts.setDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onFinish() {
                    if (downTime_firsts!=null)
                    downTime_firsts.cancelDownTimer();
                }

            });
            downTime_firsts.startDownTimer();
            mtv_title.setText(corePingEntity.brand.title);
            mtv_desc.setText(corePingEntity.brand.content);
            GlideUtils.getInstance().loadImage(baseAct,miv_photo,corePingEntity.brand.bg_img);
            GlideUtils.getInstance().loadImage(baseAct,miv_avar,corePingEntity.brand.logo);
            pingListAdapter=new PingListAdapter(baseAct,mDatas);
            rv_list.setLayoutManager(new LinearLayoutManager(baseAct,LinearLayoutManager.VERTICAL,false));
            rv_list.setNestedScrollingEnabled(false);
            rv_list.setAdapter(pingListAdapter);
            pingListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    JosnSensorsDataAPI.pinpaiStoreGoodClick(currentId,corePingEntity.brand.content
                            ,mDatas.get(position).id,mDatas.get(position).title,position,corePingEntity.brand.promotion_type);
                    GoodsDetailAct.startAct(baseAct,mDatas.get(position).id);
                }
            });
        }else {
            pingListAdapter.notifyDataSetChanged();
        }
        pingListAdapter.setPageLoading(Integer.parseInt(corePingEntity.page),Integer.parseInt(corePingEntity.total_page));
    }
}
