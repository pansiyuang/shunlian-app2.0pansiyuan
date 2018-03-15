package com.shunlian.app.ui.discover;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ArticleAdapter;
import com.shunlian.app.adapter.ChosenTagAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.presenter.ChosenPresenter;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IChosenView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DiscoverJingxuanFrag extends DiscoversFrag implements IChosenView {
    @BindView(R.id.recycler_tags)
    RecyclerView recycler_tags;

    @BindView(R.id.recycler_article)
    RecyclerView recycler_article;

    private ChosenTagAdapter mAdapter;
    private ArticleAdapter mArticleAdapter;
    private ChosenPresenter mPresenter;
    private List<ArticleEntity.Tag> mTags;
    private List<ArticleEntity.Article> mArticleList;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_jingxuan, null, false);
    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_tags.setLayoutManager(manager);
        recycler_tags.setNestedScrollingEnabled(false);

        LinearLayoutManager articleManager = new LinearLayoutManager(getActivity());
        recycler_article.setLayoutManager(articleManager);
        recycler_article.setNestedScrollingEnabled(false);
        recycler_article.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(getActivity(), 10), 0, 0));
        ((SimpleItemAnimator)recycler_article.getItemAnimator()).setSupportsChangeAnimations(false);

        mPresenter = new ChosenPresenter(getActivity(), this);
        mPresenter.getArticleList(true);
        mTags = new ArrayList<>();
        mAdapter = new ChosenTagAdapter(getActivity(), mTags);
        recycler_tags.setAdapter(mAdapter);

        mArticleList = new ArrayList<>();
        mArticleAdapter = new ArticleAdapter(getActivity(), mArticleList, this);
        recycler_article.setAdapter(mArticleAdapter);
    }


    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        return false;
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getNiceList(ArticleEntity articleEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            mTags.clear();
            mArticleList.clear();
            if (!isEmpty(articleEntity.tag_list)) {
                mTags.addAll(articleEntity.tag_list);
                mAdapter.notifyDataSetChanged();
            }
        }
        if (!isEmpty(articleEntity.article_list)) {
            mArticleList.addAll(articleEntity.article_list);
            mArticleAdapter.notifyDataSetChanged();
        }
    }

    public void toLikeArticle(String articleId) {
        mPresenter.articleLike(articleId);
    }

    public void toUnLikeArticle(String articleId) {
        mPresenter.articleUnLike(articleId);
    }


    @Override
    public void likeArticle(String articleId) {
        for (int i = 0; i < mArticleList.size(); i++) {
            ArticleEntity.Article article = mArticleList.get(i);
            if (articleId.equals(mArticleList.get(i).id)) {
                int likeCount = Integer.valueOf(article.likes) + 1;
                article.likes = likeCount + "";
                break;
            }
        }
        mArticleAdapter.updateEvaluate(articleId, "1");
    }

    @Override
    public void unLikeArticle(String articleId) {
        for (int i = 0; i < mArticleList.size(); i++) {
            ArticleEntity.Article article = mArticleList.get(i);
            if (articleId.equals(mArticleList.get(i).id)) {
                int likeCount = Integer.valueOf(article.likes) - 1;
                article.likes = likeCount + "";
                break;
            }
        }
        mArticleAdapter.updateEvaluate(articleId, "0");
    }
}
