package com.shunlian.app.ui.discover.jingxuan;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.H5CallEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.ArticleEvent;
import com.shunlian.app.presenter.ArticleDetailPresenter;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IArticleDetailView;
import com.shunlian.app.widget.MyImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/19.
 */

public class ArticleH5Act extends H5Act implements IArticleDetailView {

    @BindView(R.id.miv_favorite)
    MyImageView miv_favorite;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.tv_msg_count)
    TextView tv_msg_count;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private String articleId;


    private ArticleDetailPresenter mPresent;
    private int currentFavoriteStatus;
    private ShareInfoParam shareInfoParam;


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
        addJs("praiseBack");
        articleId = mIntent.getStringExtra("articleId");
        mPresent = new ArticleDetailPresenter(this, this);
        mPresent.getArticleDetail(articleId);
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
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.findDetail();
                quick_actions.shareInfo(shareInfoParam);
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
        if (shareInfoParam == null) {
            shareInfoParam = new ShareInfoParam();
        }
        shareInfoParam.shareLink = detailEntity.share_url;
        shareInfoParam.title = detailEntity.title;
        shareInfoParam.desc = detailEntity.full_title;
        shareInfoParam.img = detailEntity.thumb;
        shareInfoParam.thumb_type = "1";
        if (detailEntity.user_info != null){
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

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }
}
