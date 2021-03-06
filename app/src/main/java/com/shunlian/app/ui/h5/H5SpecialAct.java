package com.shunlian.app.ui.h5;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.H5CallEntity;
import com.shunlian.app.bean.ShareEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PH5;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.view.IH5View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/12/26.
 */

public class H5SpecialAct extends H5X5Act implements IH5View, MessageCountManager.OnGetMessageListener {

    private ShareInfoParam shareInfoParam;
    private MessageCountManager messageCountManager;
    private PH5 ph5;
    private String specialId;

    private ShareGoodDialogUtil shareGoodDialogUtil;
    public static void startAct(Context context, String url, int mode) {
        Intent intentH5 = new Intent(context, H5SpecialAct.class);
        intentH5.putExtra("url", url);
        intentH5.putExtra("mode", mode);
        intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentH5);
    }

    protected void jsCallback(H5CallEntity h5CallEntity) {
        super.jsCallback(h5CallEntity);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
//        String s = messageCountManager.setTextCount(tv_msg_count);
//        if (quick_actions != null)
//            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
//        String s = messageCountManager.setTextCount(tv_msg_count);
//        if (quick_actions != null)
//            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    public void onResume() {
//        if (Common.isAlreadyLogin()) {
//            messageCountManager = MessageCountManager.getInstance(this);
//            if (messageCountManager.isLoad()) {
//                String s = messageCountManager.setTextCount(tv_msg_count);
//                if (quick_actions != null)
//                    quick_actions.setMessageCount(s);
//            } else {
//                messageCountManager.initData();
//            }
//            messageCountManager.setOnGetMessageListener(this);
//        }
        super.onResume();
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
//        addJs("praiseBack");
    }

    protected void loadUrl() {
        shareGoodDialogUtil = new ShareGoodDialogUtil(this);
        if (!isEmpty(h5Url)) {
            if (h5Url.startsWith(InterentTools.H5_HOST + "special")) {
                visible(rl_title_more);
                miv_title_more.setImageResource(R.mipmap.icon_home_share);
                ph5 = new PH5(this, this);
                specialId = h5Url.substring(h5Url.indexOf("special/") + "special/".length());
                ph5.getShareInfo(specialId);
            } else if (h5Url.startsWith(InterentTools.H5_HOST + "activity")) {
                visible(rl_title_more);
                rl_title_more.setOnClickListener(v -> {
                    quick_actions.setVisibility(View.VISIBLE);
                    quick_actions.activity();
                });
            }
        }
        super.loadUrl();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginRefresh(DefMessageEvent event) {
        if (event.loginSuccess && ph5 != null) {
//            ph5.getShareInfo(ph5.special, specialId);
        }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        shareInfoParam = baseEntity.data;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void setShareInfo(ShareEntity shareEntity) {
        if (rl_title_more != null) {
            visible(rl_title_more);
            if (shareEntity == null || shareEntity.share == null) {
                rl_title_more.setVisibility(View.GONE);
            }
            rl_title_more.setOnClickListener(v -> {
//                quick_actions.setVisibility(View.VISIBLE);
                if (shareEntity != null && shareEntity.share != null) {
                    shareInfoParam = new ShareInfoParam();
                    shareInfoParam.desc = shareEntity.share.content;
                    shareInfoParam.title = shareEntity.share.title;
                    shareInfoParam.img = shareEntity.share.url;
                    shareInfoParam.shareLink = shareEntity.share.share_url;
                    shareInfoParam.special_img_url = shareEntity.share.url;
                    shareGoodDialogUtil.shareGoodDialog(shareInfoParam,false,false);
//                    quick_actions.special();
//                    quick_actions.shareInfo(shareInfoParam);
                } else {
//                    quick_actions.order();
//                   rl_title_more.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
