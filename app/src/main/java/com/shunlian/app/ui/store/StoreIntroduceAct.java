package com.shunlian.app.ui.store;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.StoreIntroducePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
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

public class StoreIntroduceAct extends BaseActivity implements StoreIntroduceView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_storeName)
    TextView mtv_storeName;

    @BindView(R.id.miv_star)
    MyImageView miv_star;

    @BindView(R.id.miv_storeLogo)
    MyImageView miv_storeLogo;

    @BindView(R.id.miv_chat)
    MyImageView miv_chat;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

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

    @BindView(R.id.mrlayout_erweima)
    MyRelativeLayout mrlayout_erweima;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;

    private boolean isFocus;
    private String storeId,storeScore,storeLogo;
    private StoreIntroducePresenter storeIntroducePresenter;
    private int focusNum=0;
    private StoreIntroduceEntity storeIntroduceEntity;

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.Store();
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(baseAct);
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

    public void mFinish(){
        Intent intent = new Intent();
        intent.putExtra("isFocus", isFocus);
        intent.putExtra("focusNum", focusNum);
        setResult(1,intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        mFinish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public static void startActForResult(Activity activity, String storeId, String storeScore,String storeLogo, boolean isFocus) {
        Intent intent = new Intent(activity, StoreIntroduceAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        intent.putExtra("storeScore", storeScore);
        intent.putExtra("storeLogo", storeLogo);
        intent.putExtra("isFocus", isFocus);

        activity.startActivityForResult(intent,0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_introduce;
    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.mtv_attention:
                if (isFocus) {
                    storeIntroducePresenter.delFollowStore(storeId);
                } else {
                    storeIntroducePresenter.followStore(storeId);
                }
                break;
            case R.id.mrlayout_yingye:
                if (storeIntroduceEntity!=null){
                    storeIntroduceEntity.isCode=false;
                    StoreLicenseAct.startAct(this,storeIntroduceEntity);
                }
                break;
            case R.id.miv_close:
                mFinish();
                break;
            case R.id.mtv_dianhua:
                if (!isEmpty(mtv_dianhua.getText()))
                initDialogs(mtv_dianhua.getText().toString());
                break;
            case R.id.mtv_weixin:
                if (mtv_weixin.getText()!=null)
                Common.copyText(this,mtv_weixin.getText().toString());
                break;
            case R.id.miv_chat:
                storeIntroducePresenter.getUserId(storeId);
                break;
            case R.id.mrlayout_erweima:
                if (storeIntroduceEntity!=null){
                    storeIntroduceEntity.isCode=true;
                    StoreLicenseAct.startAct(this,storeIntroduceEntity);
                }
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_attention.setOnClickListener(this);
        mrlayout_yingye.setOnClickListener(this);
        miv_chat.setOnClickListener(this);
        miv_close.setOnClickListener(this);
        mrlayout_erweima.setOnClickListener(this);
        mtv_dianhua.setOnClickListener(this);
        mtv_weixin.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        storeId = getIntent().getStringExtra("storeId");
        storeScore= getIntent().getStringExtra("storeScore");
        storeLogo= getIntent().getStringExtra("storeLogo");
        isFocus = getIntent().getBooleanExtra("isFocus",false);
        if (!isFocus) {
            mtv_attention.setTextColor(getResources().getColor(R.color.white));
            mtv_attention.setText(getStringResouce(R.string.discover_follow));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_n);
        } else {
            mtv_attention.setTextColor(getResources().getColor(R.color.pink_color));
            mtv_attention.setText(getStringResouce(R.string.discover_alear_follow));
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
        this.storeIntroduceEntity=storeIntroduceEntity;
        this.storeIntroduceEntity.storeScore=this.storeScore;
        this.storeIntroduceEntity.storeLogo=this.storeLogo;
        mtv_storeName.setText(storeIntroduceEntity.store_name);
        GlideUtils.getInstance().loadImage(this, miv_star, storeScore);
        GlideUtils.getInstance().loadImage(this, miv_storeLogo, storeLogo);
        if (!isEmpty(storeIntroduceEntity.store_collect))
            focusNum=Integer.parseInt(storeIntroduceEntity.store_collect);
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
            mtv_attention.setText(getStringResouce(R.string.discover_follow));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_n);
            focusNum--;
            isFocus = false;
        } else {
            mtv_attention.setTextColor(getResources().getColor(R.color.pink_color));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_h);
            mtv_attention.setText(getStringResouce(R.string.discover_alear_follow));
            focusNum++;
            isFocus = true;
        }
        mtv_number.setText(focusNum+ "人");
    }

    public void initDialogs(String phone) {
        PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener(phone, "呼叫", view -> {
            Intent intentServePhoneOne = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intentServePhoneOne);
            promptDialog.dismiss();
        }, "取消", view -> promptDialog.dismiss()).show();
    }

    @Override
    public void getUserId(String userId) {
        if (isEmpty(userId) || "0".equals(userId)) {
            Common.staticToast("该商家未开通客服");
            return;
        }
        ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();
        chatMember.shop_id = storeId;
        chatMember.m_user_id = userId;
        chatMember.type = "3";
        chatMember.nickname = mtv_storeName.getText().toString();

        ChatManager.getInstance(this).init().MemberChatToStore(chatMember);
    }
}
