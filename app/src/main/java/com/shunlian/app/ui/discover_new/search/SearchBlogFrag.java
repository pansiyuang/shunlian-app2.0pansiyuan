package com.shunlian.app.ui.discover_new.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.presenter.HotBlogPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.IHotBlogView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/24.
 */

public class SearchBlogFrag extends BaseLazyFragment implements IHotBlogView, HotBlogAdapter.OnAdapterCallBack {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private HotBlogPresenter mPresenter;
    private LinearLayoutManager manager;
    private List<BigImgEntity.Blog> blogList;
    private HotBlogAdapter hotBlogAdapter;
    private String currentKeyword;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.layout_refresh_list, null, false);
        return view;
    }

    @Override
    protected void initData() {
    }

    public static SearchBlogFrag getInstance(String str) {
        SearchBlogFrag frag = new SearchBlogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", str);
        frag.setArguments(bundle);
        return frag;
    }

    public void setKeyWord(String str) {
        currentKeyword = str;
        if (mPresenter != null) {
            mPresenter.initPage();
            mPresenter.getHotBlogList(true, "", currentKeyword);
        }
    }

    @Override
    protected void initListener() {
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.initPage();
                    mPresenter.getHotBlogList(true, "", currentKeyword);
                }
            }

            @Override
            public void onLoadMore() {

            }
        });
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        if (isEmpty(currentKeyword)) {
            currentKeyword = getArguments().getString("keyword");
        }

        blogList = new ArrayList<>();
        mPresenter = new HotBlogPresenter(getActivity(), this);
        mPresenter.getHotBlogList(true, "", currentKeyword);

        nei_empty.setImageResource(R.mipmap.img_empty_common).setText("没有搜索到相关的文章").setButtonText(null);
    }

    @Override
    public void getBlogList(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage) {
        refreshview.stopRefresh(true);
        if (currentPage == 1) {
            blogList.clear();

            if (isEmpty(hotBlogsEntity.list)) {
                nei_empty.setVisibility(View.VISIBLE);
                recycler_list.setVisibility(View.GONE);
            } else {
                nei_empty.setVisibility(View.GONE);
                recycler_list.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(hotBlogsEntity.list)) {
            blogList.addAll(hotBlogsEntity.list);
        }
        if (hotBlogAdapter == null) {
            hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, getActivity());
            recycler_list.setAdapter(hotBlogAdapter);
            hotBlogAdapter.setAdapterCallBack(this);
        }
        hotBlogAdapter.setPageLoading(currentPage, totalPage);
        hotBlogAdapter.notifyDataSetChanged();
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (memberId.equals(blog.member_id)) {
                if (blog.is_focus == 0) {
                    blog.is_focus = 1;
                } else {
                    blog.is_focus = 0;
                }
            }
        }
        hotBlogAdapter.notifyDataSetChanged();
    }

    @Override
    public void praiseBlog(String blogId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.is_praise = 1;
                blog.praise_num++;
            }
        }
        hotBlogAdapter.notifyDataSetChanged();
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
    public void toFocusUser(int isFocus, String memberId) {
        mPresenter.focusUser(isFocus, memberId);
    }

    @Override
    public void toFocusMember(int isFocus, String memberId) {

    }

    @Override
    public void toPraiseBlog(String blogId) {
        mPresenter.praiseBlos(blogId);
    }

    @Override
    public void clickMoreBtn(String blogId) {

    }
}
