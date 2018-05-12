package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreEvaluateAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.StoreIntroducePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.StoreIntroduceView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class StoreIntroduceAct extends BaseActivity implements View.OnClickListener, StoreIntroduceView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_storeName)
    TextView mtv_storeName;

    @BindView(R.id.miv_star)
    MyImageView miv_star;

    @BindView(R.id.mtv_number)
    TextView mtv_number;

    @BindView(R.id.mtv_haopinglv)
    TextView mtv_haopinglv;

    @BindView(R.id.mtv_dianhua)
    TextView mtv_dianhua;

    @BindView(R.id.mtv_weixin)
    TextView mtv_weixin;

    @BindView(R.id.mtv_baozhen)
    TextView mtv_baozhen;

    @BindView(R.id.mtv_kaidian)
    TextView mtv_kaidian;

    @BindView(R.id.rv_haopin)
    RecyclerView rv_haopin;

    @BindView(R.id.mtv_attention)
    MyTextView mtv_attention;

    @BindView(R.id.mrlayout_yingye)
    MyRelativeLayout mrlayout_yingye;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;

    private boolean isFocus;
    private String storeId,seller_id,storeScore;
    private StoreIntroducePresenter storeIntroducePresenter;

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.Store();
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

    public static void startAct(Context context, String storeId,String storeScore,boolean isFocus) {
        Intent intent = new Intent(context, StoreIntroduceAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        intent.putExtra("storeScore", storeScore);//店铺id
        intent.putExtra("isFocus", isFocus);//店铺id
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_introduce;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_attention:
                if (isFocus) {
                    storeIntroducePresenter.delFollowStore(storeId);
                } else {
                    storeIntroducePresenter.followStore(storeId);
                }
                break;
            case R.id.mrlayout_yingye:
                StoreLicenseAct.startAct(this,seller_id);
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_attention.setOnClickListener(this);
        mrlayout_yingye.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        storeId = getIntent().getStringExtra("storeId");
        storeScore= getIntent().getStringExtra("storeScore");
        isFocus = getIntent().getBooleanExtra("isFocus",false);
        if (!isFocus) {
            mtv_attention.setTextColor(getResources().getColor(R.color.white));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_n);
        } else {
            mtv_attention.setTextColor(getResources().getColor(R.color.pink_color));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_h);
        }
        storeIntroducePresenter = new StoreIntroducePresenter(this, this, storeId);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void introduceInfo(StoreIntroduceEntity storeIntroduceEntity) {
//        if ("false".equals(storeIntroduceEntity.)) {
//            mtv_attention.setTextColor(getResources().getColor(R.color.white));
//            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_n);
//            isFocus = false;
//        } else {
//            mtv_attention.setTextColor(getResources().getColor(R.color.pink_color));
//            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_h);
//            isFocus = true;
//        }
        seller_id=storeIntroduceEntity.seller_id;
        mtv_storeName.setText(storeIntroduceEntity.store_name);
        GlideUtils.getInstance().loadImage(this, miv_star, storeScore);
        mtv_number.setText(storeIntroduceEntity.store_collect + "人");
        mtv_haopinglv.setText(storeIntroduceEntity.evaluate.praise_rate);
        mtv_dianhua.setText(storeIntroduceEntity.store_phone);
        mtv_weixin.setText(storeIntroduceEntity.store_wx);
        mtv_baozhen.setText(storeIntroduceEntity.paying_amount);
        mtv_kaidian.setText(storeIntroduceEntity.store_time);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_haopin.setLayoutManager(manager);
        rv_haopin.setAdapter(new StoreEvaluateAdapter(this, false, storeIntroduceEntity.evaluate.pj));
    }

    @Override
    public void storeFocus() {
        if (isFocus) {
            mtv_attention.setTextColor(getResources().getColor(R.color.white));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_n);
            isFocus = false;
        } else {
            mtv_attention.setTextColor(getResources().getColor(R.color.pink_color));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_h);
            isFocus = true;
        }
    }
}
