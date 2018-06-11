package com.shunlian.app.ui.discover.jingxuan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.TagDetailAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.presenter.ChosenPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IChosenView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/19.
 */

public class TagDetailActivity extends BaseActivity implements IChosenView, TagDetailAdapter.OnLayoutSizeListener {

    @BindView(R.id.miv_more)
    MyImageView miv_more;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_tags)
    RecyclerView recycler_tags;

    public int offset;
    private LinearLayoutManager manager;
    private int totalDy;
    private int layoutHeight;
    private ChosenPresenter mPresent;
    private TagDetailAdapter mAdapter;
    private ArticleEntity.Tag mTag;
    private List<ArticleEntity.Article> articleList;
    private String currentId;
    private int mIndex = 1;

    public static void startAct(Context context, String tagId) {
        Intent intent = new Intent(context, TagDetailActivity.class);
        intent.putExtra("tagId", tagId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_tag_detail;
    }

    @Override
    protected void initData() {
        defToolbar();

        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        offset = toolbarParams.height;

        currentId = getIntent().getStringExtra("tagId");
        if (isEmpty(currentId)) {
            return;
        }
        mPresent = new ChosenPresenter(this, this);
        mPresent.getTagDetail(true, currentId);

        manager = new LinearLayoutManager(this);
        articleList = new ArrayList<>();
        recycler_tags.setLayoutManager(manager);
        recycler_tags.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 10), 0, 0));
        ((SimpleItemAnimator) recycler_tags.getItemAnimator()).setSupportsChangeAnimations(false);
    }


    @Override
    protected void initListener() {
        super.initListener();
        recycler_tags.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    View firstView = manager.findViewByPosition(firstPosition);
                    if (firstView instanceof RelativeLayout) {
                        totalDy += dy;
                        setBgColor(totalDy);
                    } else {
                        setToolbar();
                        totalDy = layoutHeight;
                    }
                }
            }
        });
    }

    public void defToolbar() {
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.titleBar(toolbar, false)
                .statusBarDarkFont(true, 0f)
                .addTag(GoodsDetailAct.class.getName()).init();
    }

    public void setBgColor(int totalDy) {
        ImmersionBar immersionBar = ImmersionBar.with(this).addViewSupportTransformColor(toolbar, R.color.white);
        if (totalDy <= layoutHeight) {
            if (totalDy <= 0) {
                totalDy = 0;
            }
            float alpha = (float) totalDy / layoutHeight;
            immersionBar.statusBarAlpha(alpha).addTag(GoodsDetailAct.class.getName()).init();
            tv_title.setAlpha(alpha);
            float v = 1.0f - alpha * 2;
            if (v <= 0) {
                v = alpha * 2 - 1;
                setImg(2, 1);
            } else {
                setImg(1, 2);
            }
            miv_close.setAlpha(v);
            miv_more.setAlpha(v);
        } else {
            setToolbar();
        }
    }

    public void setToolbar() {
        setImg(2, 1);
        immersionBar.statusBarAlpha(1.0f).addTag(GoodsDetailAct.class.getName()).init();
        miv_close.setAlpha(1.0f);
        miv_more.setAlpha(1.0f);
        tv_title.setAlpha(1.0f);
    }

    private void setImg(int status, int oldStatus) {
        if (status != oldStatus) {
            if (status == 1) {
                miv_close.setImageResource(R.mipmap.icon_more_fanhui);
                miv_more.setImageResource(R.mipmap.icon_more_gengduo);
            } else {
                miv_close.setImageResource(R.mipmap.img_more_fanhui_n);
                miv_more.setImageResource(R.mipmap.icon_more_n);
            }
        }
    }

    @Override
    public void getNiceList(ArticleEntity articleEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            articleList.clear();
            mTag = articleEntity.tag_obj;
            tv_title.setText(mTag.name);
        }
        if (!isEmpty(articleEntity.article_list)) {
            articleList.addAll(articleEntity.article_list);
            List<ArticleEntity.Topic> topicList = articleEntity.topic_list;
            int index = 0;
            if (articleEntity.article_list.size() >= 5) {
                index = 4;
            } else if (articleEntity.article_list.size() < 5) {
                index = articleEntity.article_list.size() - 1;
            }
            articleEntity.article_list.get(index).topic_list = topicList;
        }
        if (mAdapter == null) {
            mAdapter = new TagDetailAdapter(this, articleList, mTag);
            mAdapter.setOnLayoutSizeListener(this);
            recycler_tags.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((view, position) -> {
                try {
                    ArticleEntity.Article article = articleList.get(position - 1);
                    ArticleH5Act.startAct(TagDetailActivity.this, article.id, ArticleH5Act.MODE_SONIC);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        mAdapter.notifyDataSetChanged();
    }

    public void toLikeArticle(String articleId) {
        mPresent.articleLike(articleId);
    }

    public void toUnLikeArticle(String articleId) {
        mPresent.articleUnLike(articleId);
    }

    public void toGetOtherTopiscList() {
        mPresent.getOthersTopicList(String.valueOf(mIndex));
    }


    @Override
    public void likeArticle(String articleId) {
        for (int i = 0; i < articleList.size(); i++) {
            ArticleEntity.Article article = articleList.get(i);
            if (articleId.equals(articleList.get(i).id)) {
                int likeCount = Integer.valueOf(article.likes) + 1;
                article.likes = likeCount + "";
                break;
            }
        }
        mAdapter.updateEvaluate(articleId, "1");
    }

    @Override
    public void unLikeArticle(String articleId) {
        for (int i = 0; i < articleList.size(); i++) {
            ArticleEntity.Article article = articleList.get(i);
            if (articleId.equals(articleList.get(i).id)) {
                int likeCount = Integer.valueOf(article.likes) - 1;
                article.likes = likeCount + "";
                break;
            }
        }
        mAdapter.updateEvaluate(articleId, "0");
    }

    @Override
    public void getOtherTopics(List<ArticleEntity.Topic> topic_list) {
        mAdapter.notityTopicData(topic_list);
        mIndex++;
    }

    @Override
    public void refreshFinish() {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void OnTopSize(int height) {
        layoutHeight = height - offset - ImmersionBar.getStatusBarHeight(this);
    }
}