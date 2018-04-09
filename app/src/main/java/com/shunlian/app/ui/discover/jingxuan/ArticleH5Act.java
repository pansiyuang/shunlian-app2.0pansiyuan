package com.shunlian.app.ui.discover.jingxuan;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.H5CallEntity;
import com.shunlian.app.eventbus_bean.ArticleEvent;
import com.shunlian.app.presenter.ArticleDetailPresenter;
import com.shunlian.app.ui.h5.H5Act;
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


    private String articleId;


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
        addJs("praiseBack");
        articleId = mIntent.getStringExtra("articleId");
        mPresent = new ArticleDetailPresenter(this, this);
        mPresent.getArticleDetail(articleId);
    }

    @Override
    protected void initListener() {
        miv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFavoriteStatus == 1) {
                    mPresent.unFavoriteArticle(articleId);
                } else {
                    mPresent.favoriteArticle(articleId);
                }
            }
        });
        super.initListener();
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


}
