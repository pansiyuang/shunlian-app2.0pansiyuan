package com.shunlian.app.ui.discover.jingxuan;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.H5CallEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.ArticleEvent;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.eventbus_bean.ShareInfoEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.ArticleDetailPresenter;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IArticleDetailView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2018/3/19.
 */

public class ArticleH5Act extends H5Act implements IArticleDetailView, MessageCountManager.OnGetMessageListener {

    private String articleId;

    private MessageCountManager messageCountManager;
    private ArticleDetailPresenter mPresent;
    private int currentFavoriteStatus;
    private ShareInfoParam shareInfoParam;


    public static void startAct(Context context, String articleId, int mode) {
        Intent intentH5 = new Intent(context, ArticleH5Act.class);
        intentH5.putExtra("articleId", articleId);
        intentH5.putExtra("mode", mode);
        intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentH5);
    }

    @Override
    protected void jsCallback(H5CallEntity h5CallEntity) {
        EventBus.getDefault().post(new ArticleEvent(articleId, h5CallEntity.istates));
    }


    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        miv_favorite.setVisibility(View.VISIBLE);
        rl_title_more.setVisibility(View.VISIBLE);
        addJs("praiseBack");
        articleId = mIntent.getStringExtra("articleId");
        mPresent = new ArticleDetailPresenter(this, this);
        mPresent.getArticleDetail(articleId);
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
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


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initListener() {
        miv_favorite.setOnClickListener(this);
        rl_title_more.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.miv_favorite:
                if (!Common.isAlreadyLogin()) {
                    Common.goGoGo(this, "login");
                    return;
                }
                if (currentFavoriteStatus == 1) {
                    mPresent.unFavoriteArticle(articleId);
                } else {
                    mPresent.favoriteArticle(articleId);
                }
                break;
            case R.id.rl_title_more:
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.findDetail(articleId);
                quick_actions.shareInfo(shareInfoParam);
                break;
        }
        super.onClick(view);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginRefresh(DefMessageEvent event) {
        if (event.loginSuccess && mPresent != null) {
            mPresent.getShareInfo(mPresent.article, articleId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shareSuccess(ShareInfoEvent event){
        if (event.isShareSuccess&&Common.isForeground(this,getClass().getName())){
            oget.setEggsCount(event.eggs_count);
            oget.show(4000);
        }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        shareInfoParam.shareLink = baseEntity.data.shareLink;
        shareInfoParam.userName = baseEntity.data.userName;
        shareInfoParam.userAvatar = baseEntity.data.userAvatar;
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getArticleDetail(ArticleDetailEntity detailEntity) {
        if (!isEmpty(detailEntity.h5_detail_url)) {
            try {
                detailEntity.h5_detail_url = java.net.URLDecoder.decode(detailEntity.h5_detail_url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            h5Url = detailEntity.h5_detail_url;
            reFresh();
        }

        if ("1".equals(detailEntity.had_favorites)) {
            miv_favorite.setImageResource(R.mipmap.icon_found_souchang_h);
        } else {
            miv_favorite.setImageResource(R.mipmap.icon_found_souchang_n);
        }
        currentFavoriteStatus = Integer.valueOf(detailEntity.had_favorites);
        miv_favorite.setVisibility(View.VISIBLE);
        if (shareInfoParam == null) {
            shareInfoParam = new ShareInfoParam();
        }
        shareInfoParam.shareLink = detailEntity.share_url;
        shareInfoParam.title = detailEntity.title;
        shareInfoParam.desc = detailEntity.full_title;
        shareInfoParam.img = detailEntity.thumb;
        shareInfoParam.isCopyTitle = true;
        shareInfoParam.thumb_type = "1";
        if (detailEntity.user_info != null) {
            shareInfoParam.userName = detailEntity.user_info.nickname;
            shareInfoParam.userAvatar = detailEntity.user_info.avatar;
        }

    }

    @Override
    public void favoriteSuccess() {
        currentFavoriteStatus = 1;
        miv_favorite.setImageResource(R.mipmap.icon_found_souchang_h);
    }

    @Override
    public void unFavoriteSuccess() {
        currentFavoriteStatus = 0;
        miv_favorite.setImageResource(R.mipmap.icon_found_souchang_n);
    }

}
