package com.shunlian.app.ui.discover;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ArticleAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.ChosenTagAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.presenter.ChosenPresenter;
import com.shunlian.app.ui.h5.ArticleH5Act;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IChosenView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DiscoverJingxuanFrag extends DiscoversFrag implements IChosenView, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.recycler_tags)
    RecyclerView recycler_tags;

    @BindView(R.id.recycler_article)
    RecyclerView recycler_article;

    private ArticleAdapter mArticleAdapter;
    private ChosenPresenter mPresenter;
    private List<ArticleEntity.Tag> mTags;
    private List<ArticleEntity.Article> mArticleList;
    private int mIndex = 1;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_jingxuan, container, false);
    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_tags.setLayoutManager(manager);
        recycler_tags.setNestedScrollingEnabled(false);

        LinearLayoutManager articleManager = new LinearLayoutManager(getActivity());
        recycler_article.setLayoutManager(articleManager);
        recycler_article.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(getActivity(), 10), 0, 0));
        ((SimpleItemAnimator) recycler_article.getItemAnimator()).setSupportsChangeAnimations(false);

        mPresenter = new ChosenPresenter(getActivity(), this);
        mPresenter.getArticleList(true);
        mTags = new ArrayList<>();


        mArticleList = new ArrayList<>();
        recycler_article.setAdapter(mArticleAdapter);
        mArticleAdapter.setOnItemClickListener(this);
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
            }

            if (!isEmpty(articleEntity.article_list)) {
                List<ArticleEntity.Topic> topicList = articleEntity.topic_list;
                int index = 0;
                if (articleEntity.article_list.size() >= 5) {
                    index = 4;
                } else if (articleEntity.article_list.size() < 5) {
                    index = articleEntity.article_list.size() - 1;
                }
                articleEntity.article_list.get(index).topic_list = topicList;
            }

            mArticleAdapter = new ArticleAdapter(getActivity(), mArticleList, this, mTags);
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

    public void toGetOtherTopiscList() {
        mPresenter.getOthersTopicList(String.valueOf(mIndex));
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

    @Override
    public void getOtherTopics(List<ArticleEntity.Topic> topic_list) {
        mArticleAdapter.notityTopicData(topic_list);
        mIndex++;
    }

    @Override
    public void onItemClick(View view, int position) {
        ArticleEntity.Article article = mArticleList.get(position);
        ArticleH5Act.startAct(getActivity(), article.id, ArticleH5Act.MODE_SONIC);
    }
}
