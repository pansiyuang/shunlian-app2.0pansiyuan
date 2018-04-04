package com.shunlian.app.ui.collection;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.ContentAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.presenter.ContentPresenter;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IContentView;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22.
 * 收藏内容
 */

public class CollectionContentFrag extends CollectionFrag implements IContentView, ContentAdapter.OnSelectListener, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.recycler_content)
    RecyclerView recycler_content;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    private ContentPresenter mPresenter;
    private ContentAdapter mAdapter;
    private LinearLayoutManager manager;
    private List<ArticleEntity.Article> mArticleList;
    private boolean isEdit;
    private int selectStatus = 0; // 0 全选  1 部分选择 2 全不选
    private boolean isSelectAll;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_collection_content, container, false);
    }

    @Override
    protected void initData() {
        mPresenter = new ContentPresenter(getActivity(), this);
        mPresenter.getFavoriteArticles(true);

        //新增下拉刷新
        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initPage();
                mPresenter.getFavoriteArticles(true);
            }

            @Override
            public void onLoadMore() {

            }
        });

        ((SimpleItemAnimator) recycler_content.getItemAnimator()).setSupportsChangeAnimations(false); //解决刷新item图片闪烁的问题
        recycler_content.setNestedScrollingEnabled(false);
        mArticleList = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        recycler_content.setLayoutManager(manager);
    }

    @Override
    protected void initListener() {
        recycler_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        super.initListener();
    }

    /**
     * 删除
     */
    @Override
    public void delete() {
        String result = getRemoveIds();
        if (isEmpty(result)) {
            return;
        }
        mPresenter.removeArticles(result, false);
    }

    /**
     * 选择所有
     */
    @Override
    public void selectAll() {
        if (isSelectAll) {
            isSelectAll = false;
        } else {
            isSelectAll = true;
        }
        toSelectAll(isSelectAll);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 操作管理
     */
    @Override
    public void operationMange() {
        if (mAdapter != null) {
            isSelectAll = false;
            selectStatus = 2;
            toSelectAll(false);
            mAdapter.setEditMode(false);
        }
    }

    /**
     * 完成管理
     */
    @Override
    public void finishManage() {
        if (mAdapter != null) {
            isEdit = false;
            toSelectAll(false);
            mAdapter.setEditMode(isEdit);
            isSelectAll = false;
            selectStatus = 2;
            ((MyCollectionAct) baseActivity).setManageState(selectStatus);
        }
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        if (!isEmpty(mArticleList) && mAdapter != null) {
            if (!mAdapter.getEditMode()) {
                mAdapter.setEditMode(true);
            } else {
                mAdapter.setEditMode(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void getFavoriteArticles(List<ArticleEntity.Article> articleList, int page, int totalPage) {
        if (page == 1) {
            mArticleList.clear();
        }
        if (!isEmpty(articleList)) {
            for (ArticleEntity.Article article : articleList) {
                article.isSelect = isSelectAll;
            }
            mArticleList.addAll(articleList);
        }
        if (mAdapter == null) {
            mAdapter = new ContentAdapter(getActivity(), mArticleList);
            recycler_content.setAdapter(mAdapter);
            mAdapter.setOnSelectListener(this);
            mAdapter.setOnItemClickListener(this);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshFinish() {
        refreshview.stopRefresh(true);
    }

    @Override
    public void praiseExperience() {

    }

    @Override
    public void removeItem(String articleId) {
        for (int i = 0; i < mArticleList.size(); i++) {
            if (articleId.equals(mArticleList.get(i).id)) {
                mArticleList.remove(i);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeSuccess() {
        Iterator<ArticleEntity.Article> it = mArticleList.iterator();
        while (it.hasNext()) {
            ArticleEntity.Article article = it.next();
            if (article.isSelect) {
                it.remove();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onSelect(int position) {
        if (mArticleList.get(position).isSelect) {//取消选中
            mArticleList.get(position).isSelect = false;
            if (checkUnSelectAll()) {
                selectStatus = 2;
            } else {
                selectStatus = 1;
            }
            isSelectAll = false;
        } else { //选中
            mArticleList.get(position).isSelect = true;
            if (checkSelectAll()) { //是否全选中
                selectStatus = 0;
                isSelectAll = true;
            } else {
                selectStatus = 1;
                isSelectAll = false;
            }
        }
        mAdapter.notifyItemChanged(position);
        ((MyCollectionAct) baseActivity).setManageState(selectStatus);
    }

    @Override
    public void onDel(int position) {
        ArticleEntity.Article article = mArticleList.get(position);
        if (isEmpty(article.id)) {
            return;
        }
        mPresenter.removeArticles(article.id, true);
    }

    /**
     * 检查是否全部选中
     *
     * @return
     */
    public boolean checkSelectAll() {
        for (ArticleEntity.Article article : mArticleList) {
            if (!article.isSelect) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否全未选中
     *
     * @return
     */
    public boolean checkUnSelectAll() {
        for (ArticleEntity.Article article : mArticleList) {
            if (article.isSelect) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        ArticleEntity.Article article = mArticleList.get(position);
        if ("invalid".equals(article.status)) {
            Common.staticToast("该文章已失效");
            return;
        }
        ArticleH5Act.startAct(getActivity(), article.id, ArticleH5Act.MODE_SONIC);
    }

    /**
     * 选择/反选所有
     */

    public void toSelectAll(boolean isSelectAll) {
        for (ArticleEntity.Article article : mArticleList) {
            article.isSelect = isSelectAll;
        }
        if (isSelectAll) {
            selectStatus = 0;
        } else {
            selectStatus = 2;
        }
    }

    public String getRemoveIds() {
        if (isEmpty(mArticleList)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mArticleList.size(); i++) {
            ArticleEntity.Article article = mArticleList.get(i);
            if (article.isSelect) {
                stringBuffer.append(article.id);
                stringBuffer.append(",");
            }
        }
        String result = stringBuffer.toString();
        result = result.substring(0, result.length() - 1);
        return result;
    }
}
