package com.shunlian.app.ui.discover;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ArticleAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.ArticleEvent;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.presenter.ChosenPresenter;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IChosenView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DiscoverJingxuanFrag extends DiscoversFrag implements IChosenView, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.recycler_article)
    RecyclerView recycler_article;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.nsv_bootom)
    NestedScrollView nsv_bootom;

    private ArticleAdapter mArticleAdapter;
    private ChosenPresenter mPresenter;
    private List<ArticleEntity.Tag> mTags;
    private List<ArticleEntity.Article> mArticleList;
    private int mIndex = 1;
    private LinearLayoutManager articleManager;
    private QuickActions quick_actions;
    private ShareInfoParam mShareInfoParam;
    private String mArticleId;
    private List<String> imgList;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_jingxuan, container, false);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        NestedSlHeader header = new NestedSlHeader(baseContext);
        lay_refresh.setRefreshHeaderView(header);

        articleManager = new LinearLayoutManager(getActivity());
        recycler_article.setLayoutManager(articleManager);
        ((SimpleItemAnimator) recycler_article.getItemAnimator()).setSupportsChangeAnimations(false);

        mPresenter = new ChosenPresenter(getActivity(), this);
        mPresenter.getArticleList(true);
        mTags = new ArrayList<>();
        mArticleList = new ArrayList<>();
        imgList = new ArrayList<>();

        //分享
        quick_actions = new QuickActions(baseActivity);
        ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        decorView.addView(quick_actions);
        quick_actions.setVisibility(View.INVISIBLE);

        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText("暂时没有用户发布精选文章")
                .setButtonText(null);
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                mPresenter.initPage();
                mPresenter.getArticleList(true);
            }
        });

        recycler_article.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (articleManager != null) {
                    int lastPosition = articleManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == articleManager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });

        super.initListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(ArticleEvent event) {
        if (!isEmpty(event.articleId) && !isEmpty(event.isLike)) {
            if ("1".equals(event.isLike)) {
                upDateLikeArticle(event.articleId);
            } else {
                upDateUnLikeArticle(event.articleId);
            }
        }
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
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
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
                recycler_article.setVisibility(View.VISIBLE);
                nsv_bootom.setVisibility(View.GONE);
            } else {
                recycler_article.setVisibility(View.GONE);
                nsv_bootom.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(articleEntity.article_list)) {
            mArticleList.addAll(articleEntity.article_list);
        }
        if (mArticleAdapter == null) {
            mArticleAdapter = new ArticleAdapter(getActivity(), mArticleList, this, mTags);
            mArticleAdapter.setOnItemClickListener(this);
            recycler_article.setAdapter(mArticleAdapter);
        }
        mArticleAdapter.setPageLoading(currentPage, totalPage);
        mArticleAdapter.notifyDataSetChanged();
    }

    //分享文章方法
    public void shareArticle(int position) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(baseActivity, "login");
            return;
        }
        ArticleEntity.Article article = mArticleList.get(position - 1);

        if (mPresenter != null) {
            mShareInfoParam = mPresenter.getShareInfoParam();
            mShareInfoParam.title = article.title;
            mShareInfoParam.desc = article.full_title;
            mShareInfoParam.img = article.thumb;
            mShareInfoParam.thumb_type = article.thumb_type;
            mShareInfoParam.video_url = article.video_url;
            mArticleId = article.id;

            if ("2".equals(article.thumb_type)) {
                for (String s : article.thumb_list) {
                    imgList.add(s);
                }
                mShareInfoParam.downloadPic = (ArrayList<String>) imgList;
            }

            if (!isEmpty(article.share_url)) {
                mShareInfoParam.shareLink = article.share_url;
                share();
            } else {
                mPresenter.getShareInfo(mPresenter.nice, article.id);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginRefresh(DefMessageEvent event) {
        if (event.loginSuccess && mPresenter != null) {
            mPresenter.getShareInfo(mPresenter.nice, mArticleId);
        }
    }

    private void share() {
        if (quick_actions != null) {
            visible(quick_actions);
            quick_actions.shareInfo(mShareInfoParam);
            quick_actions.shareStyle2Dialog(true, 2, "article", mArticleId);
        }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        mShareInfoParam.shareLink = baseEntity.data.shareLink;
        mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
        mShareInfoParam.userName = baseEntity.data.userName;
        share();
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
        upDateLikeArticle(articleId);
    }

    @Override
    public void unLikeArticle(String articleId) {
        upDateUnLikeArticle(articleId);
    }

    public void upDateLikeArticle(String articleId) {
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

    public void upDateUnLikeArticle(String articleId) {
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
        ArticleEntity.Article article = mArticleList.get(position - 1);
        ArticleH5Act.startAct(getActivity(), article.id, ArticleH5Act.MODE_SONIC);
    }

    /**
     * 刷新完成
     */
    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        EventBus.getDefault().unregister(this);
    }
}
