package com.shunlian.app.ui.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.PinpaiAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PAishang;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IAishang;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.popmenu.PopMenu;
import com.shunlian.app.widget.popmenu.PopMenuItem;
import com.shunlian.app.widget.popmenu.PopMenuItemCallback;
import com.shunlian.app.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/7.
 */

public class PingpaiAct extends BaseActivity implements View.OnClickListener, IAishang, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.kanner)
    MyKanner kanner;


    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;
    private ShareInfoParam mShareInfoParam = new ShareInfoParam();
    private PAishang pAishang;
    private PinpaiAdapter pinpaiAdapter;
    private List<CorePingEntity.MData> pingList;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, PingpaiAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

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
                if (quick_actions != null)
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
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_msg_count);
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

    @Override
    protected int getLayoutId() {
        return R.layout.act_ping_pai;
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        mtv_title.setText(getStringResouce(R.string.first_pingpaite));
        pAishang = new PAishang(this, this);
        pAishang.getCorePing();
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
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
    public void setPushData(List<HotRdEntity.MData> mData, HotRdEntity data) {

    }

    @Override
    public void setPingData(CorePingEntity corePingEntity) {
        if (corePingEntity.banner != null && corePingEntity.banner.size() > 0) {
            kanner.setVisibility(View.VISIBLE);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < corePingEntity.banner.size(); i++) {
                strings.add(corePingEntity.banner.get(i).img);
                if (i >= corePingEntity.banner.size() - 1) {
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                        @Override
                        public void onItemClick(int position) {
                            Common.goGoGo(getBaseContext(), corePingEntity.banner.get(position).type, corePingEntity.banner.get(position).item_id);
                        }
                    });
                }
            }
        } else {
            kanner.setVisibility(View.GONE);
        }
        if (isEmpty(corePingEntity.brand_list)) {
            visible(nei_empty);
            gone(rv_list);
        } else {
            gone(nei_empty);
            visible(rv_list);
        }
        pingList = corePingEntity.brand_list;
        rv_list.setNestedScrollingEnabled(false);
        rv_list.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        pinpaiAdapter = new PinpaiAdapter(this, true, corePingEntity.brand_list);
        rv_list.setAdapter(pinpaiAdapter);
        pinpaiAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PingpaiListAct.startAct(getBaseContext(), corePingEntity.brand_list.get(position).id);
            }
        });
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        mShareInfoParam.desc = baseEntity.data.desc;
        mShareInfoParam.img = baseEntity.data.userAvatar;
        mShareInfoParam.shareLink = baseEntity.data.shareLink;
        mShareInfoParam.title = baseEntity.data.title;

        shareStyle2Dialog();
    }

    public void share(String id, CorePingEntity.Share param) {
        if (isEmpty(param.share_url)) {
            pAishang.getShareInfo(pAishang.sale, id);
        } else {
            mShareInfoParam.desc = param.content;
            mShareInfoParam.img = param.logo;
            mShareInfoParam.shareLink = param.share_url;
            mShareInfoParam.title = param.title;
            shareStyle2Dialog();
        }
    }

    /**
     * 分享微信和复制链接
     */
    public void shareStyle2Dialog() {
        PopMenu mPopMenu = new PopMenu.Builder().attachToActivity(this)
                .addMenuItem(new PopMenuItem("微信", getDrawableResouce(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem("复制链接", getDrawableResouce(R.mipmap.icon_lianjie)))
                .setOnItemClickListener(new PopMenuItemCallback() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                WXEntryActivity.startAct(PingpaiAct.this, "shareFriend", mShareInfoParam);
                                break;
                            case 1:
                                Common.copyText(PingpaiAct.this, mShareInfoParam.shareLink, mShareInfoParam.desc, true);
                                break;
                        }
                    }
                }).build();
        if (!mPopMenu.isShowing()) {
            mPopMenu.show();
        }
    }
}
