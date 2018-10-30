package com.shunlian.app.ui.discover_new;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.presenter.CommonBlogPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICommonBlogView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class CommonBlogFrag extends BaseLazyFragment implements ICommonBlogView, HotBlogAdapter.OnAdapterCallBack, HotBlogAdapter.OnDelBlogListener, HotBlogAdapter.OnFavoListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private CommonBlogPresenter mPresenter;
    private String currentFrom, currentType, currentMemberId;
    private HotBlogAdapter hotBlogAdapter;
    private List<BigImgEntity.Blog> blogList;
    private LinearLayoutManager manager;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_common_blog, null, false);
        return view;
    }

    public static BaseFragment getInstance(String comFrom, String memberId) {
        CommonBlogFrag commonBlogFrag = new CommonBlogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("from", comFrom);
        bundle.putString("member_id", memberId);
        commonBlogFrag.setArguments(bundle);
        return commonBlogFrag;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        NestedSlHeader header = new NestedSlHeader(getActivity());
        lay_refresh.setRefreshHeaderView(header);

        currentMemberId = getArguments().getString("member_id");
        currentFrom = getArguments().getString("from");

        if ("我的".equals(currentFrom)) {
            currentType = "2";
            nei_empty.setImageResource(R.mipmap.img_empty_faxian)
                    .setText("空空如也，还没有分享过内容")
                    .setButtonText(null);
        } else if ("收藏".equals(currentFrom)) {
            currentType = "3";
            nei_empty.setImageResource(R.mipmap.img_empty_faxian)
                    .setText("空空如也，还没有收藏过内容")
                    .setButtonText(null);
        }

        mPresenter = new CommonBlogPresenter(getActivity(), this);
        mPresenter.getBlogList(true, currentMemberId, currentType);

        blogList = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                mPresenter.initPage();
                mPresenter.getBlogList(true, currentMemberId, currentType);
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
    public void getFocusblogs(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            blogList.clear();

            if (isEmpty(hotBlogsEntity.list)) {
                recycler_list.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
            } else {
                recycler_list.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            }

            if (getActivity() instanceof MyPageActivity) {
                ((MyPageActivity) getActivity()).initInfo(hotBlogsEntity.member_info, hotBlogsEntity.discovery_info);
            }
        }
        if (!isEmpty(hotBlogsEntity.list)) {
            blogList.addAll(hotBlogsEntity.list);
        }
        if (hotBlogAdapter == null) {
            hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, getActivity());
            recycler_list.setAdapter(hotBlogAdapter);
            hotBlogAdapter.setAdapterCallBack(this);
            hotBlogAdapter.setOnDelListener(this);
            hotBlogAdapter.setOnFavoListener(this);
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
    public void showFailureView(int request_code) {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
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
    public void onDel(String blogId) {
        for (int i = 0; i < blogList.size(); i++) {
            BigImgEntity.Blog blog = blogList.get(i);
            if (blogId.equals(blog.id)) {
                blogList.remove(i);
            }
        }
        if (isEmpty(blogList)) {
            recycler_list.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnFavo(int isFavo, String blogId) {
        for (int i = 0; i < blogList.size(); i++) {
            BigImgEntity.Blog blog = blogList.get(i);
            if (blogId.equals(blog.id) && isFavo == 0) {
                blogList.remove(i);
            }
        }
        if (isEmpty(blogList)) {
            recycler_list.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }
    }
}
