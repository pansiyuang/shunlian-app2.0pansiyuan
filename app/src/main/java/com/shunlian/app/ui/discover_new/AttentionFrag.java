package com.shunlian.app.ui.discover_new;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.presenter.AttentionPresenter;
import com.shunlian.app.presenter.HotBlogPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IAttentionView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class AttentionFrag extends BaseLazyFragment implements IAttentionView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private AttentionPresenter mPresenter;
    private HotBlogAdapter hotBlogAdapter;
    private List<HotBlogsEntity.Blog> blogList;
    private LinearLayoutManager manager;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_attention, null, false);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        NestedSlHeader header = new NestedSlHeader(getContext());
        lay_refresh.setRefreshHeaderView(header);
        recycler_list.setNestedScrollingEnabled(false);

        blogList = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        nei_empty.setImageResource(R.mipmap.image_cg)
                .setText("你还没有关注任何人")
                .setButtonText(null);

        mPresenter = new AttentionPresenter(getActivity(), this);
        mPresenter.getFocusblogs(true);
    }


    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            mPresenter.initPage();
            mPresenter.getFocusblogs(true);
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

            LogUtil.httpLogW("getFocusblogs:" + hotBlogsEntity.list.size());
            if (isEmpty(hotBlogsEntity.list)) {
                nei_empty.setVisibility(View.VISIBLE);
                recycler_list.setVisibility(View.GONE);
            } else {
                nei_empty.setVisibility(View.GONE);
                recycler_list.setVisibility(View.VISIBLE);
            }
        }
        if (isEmpty(hotBlogsEntity.list)) {
            blogList.addAll(hotBlogsEntity.list);
        }
        if (hotBlogAdapter == null) {
            hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, getActivity());
            recycler_list.setAdapter(hotBlogAdapter);
        }
        hotBlogAdapter.setPageLoading(currentPage, totalPage);
        hotBlogAdapter.notifyDataSetChanged();
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
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
