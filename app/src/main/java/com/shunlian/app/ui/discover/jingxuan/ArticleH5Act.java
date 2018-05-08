package com.shunlian.app.ui.discover.jingxuan;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.H5CallEntity;
import com.shunlian.app.eventbus_bean.ArticleEvent;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.ArticleDetailPresenter;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.view.IArticleDetailView;
import com.shunlian.app.widget.MyImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/19.
 */

public class ArticleH5Act extends H5Act implements IArticleDetailView, MessageCountManager.OnGetMessageListener {

    @BindView(R.id.miv_favorite)
    MyImageView miv_favorite;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.tv_msg_count)
    TextView tv_msg_count;


    private String articleId;

    private MessageCountManager messageCountManager;
    private ArticleDetailPresenter mPresent;
    private int currentFavoriteStatus;



    public static void startAct(Context context, String articleId, int mode) {
        Intent intentH5 = new Intent(context, ArticleH5Act.class);
        intentH5.putExtra("articleId", articleId);
        intentH5.putExtra("mode", mode);
        intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
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

    @Override
    protected void onResume() {
        if (messageCountManager.isLoad()) {
            messageCountManager.setTextCount(tv_msg_count);
        } else {
            messageCountManager.initData();
        }
        super.onResume();
    }

    @Override
    protected void initListener() {
        miv_favorite.setOnClickListener(this);
        rl_title_more.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.miv_favorite:
                if (currentFavoriteStatus == 1) {
                    mPresent.unFavoriteArticle(articleId);
                } else {
                    mPresent.favoriteArticle(articleId);
                }
                break;
            case R.id.rl_title_more:
                break;
        }
        super.onClick(view);
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
            h5Url = detailEntity.h5_detail_url;
            initSonic();
            loadUrl();
        }

        if ("1".equals(detailEntity.had_favorites)) {
            miv_favorite.setImageResource(R.mipmap.icon_found_souchang_h);
        } else {
            miv_favorite.setImageResource(R.mipmap.icon_found_souchang_n);
        }
        currentFavoriteStatus = Integer.valueOf(detailEntity.had_favorites);
        miv_favorite.setVisibility(View.VISIBLE);
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


    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    public void OnLoadFail() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
