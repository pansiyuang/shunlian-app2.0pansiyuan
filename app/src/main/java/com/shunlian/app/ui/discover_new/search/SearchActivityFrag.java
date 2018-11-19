package com.shunlian.app.ui.discover_new.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DiscoverActivityAdapter;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.presenter.ActivityPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.ui.discover_new.ActivityDetailActivity;
import com.shunlian.app.view.IActivityView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/24.
 */

public class SearchActivityFrag extends BaseLazyFragment implements IActivityView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private ActivityPresenter mPresenter;
    private LinearLayoutManager manager;
    private DiscoverActivityAdapter mAdapter;
    private List<DiscoverActivityEntity.Activity> activityList;
    private String currentKeyword;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.layout_refresh_list, null, false);
        return view;
    }

    @Override
    protected void initData() {
    }

    public static SearchActivityFrag getInstance(String str) {
        SearchActivityFrag frag = new SearchActivityFrag();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", str);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    protected void initListener() {
        super.initListener();
        lay_refresh.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                mPresenter.initPage();
                mPresenter.getActivities(true, currentKeyword, "");
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
    }

    public void setKeyWord(String str) {
        currentKeyword = str;
        if (mPresenter != null) {
            mPresenter.initPage();
            mPresenter.getActivities(true, currentKeyword, "1");
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        NestedSlHeader header = new NestedSlHeader(baseContext);
        lay_refresh.setRefreshHeaderView(header);

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        if (isEmpty(currentKeyword)) {
            currentKeyword = getArguments().getString("keyword");
        }

        mPresenter = new ActivityPresenter(getActivity(), this);
        mPresenter.getActivities(true, currentKeyword, "1");
        activityList = new ArrayList<>();

        nei_empty.setImageResource(R.mipmap.img_empty_common).setText("没有搜索到相关的活动").setButtonText(null);
    }

    @Override
    public void getActivities(DiscoverActivityEntity activityEntity, int page, int totalPage) {
        if (page == 1) {
            activityList.clear();

            if (isEmpty(activityEntity.list)) {
                nei_empty.setVisibility(View.VISIBLE);
                lay_refresh.setVisibility(View.GONE);
            } else {
                nei_empty.setVisibility(View.GONE);
                lay_refresh.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(activityEntity.list)) {
            activityList.addAll(activityEntity.list);
        }
        if (mAdapter == null) {
            mAdapter = new DiscoverActivityAdapter(getActivity(), activityList);
            recycler_list.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((view, position) -> {
                ActivityDetailActivity.startAct(getActivity(), activityList.get(position).id);
            });
        }
        mAdapter.setPageLoading(page, totalPage);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
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
}
