package com.shunlian.app.ui.discover_new;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DiscoverActivityAdapter;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.presenter.ActivityPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.IActivityView;
import com.shunlian.app.view.IView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class ActivityFrag extends BaseLazyFragment implements IView, IActivityView {

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

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_frag, null, false);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            mPresenter.initPage();
            mPresenter.getActivities(true);
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
        NestedSlHeader header = new NestedSlHeader(getContext());
        lay_refresh.setRefreshHeaderView(header);
        recycler_list.setNestedScrollingEnabled(false);

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        mPresenter = new ActivityPresenter(getActivity(), this);
        mPresenter.getActivities(true, "", "1");
        activityList = new ArrayList<>();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getActivities(DiscoverActivityEntity activityEntity, int page, int totalPage) {
        if (page == 1) {
            activityList.clear();
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

    /**
     * 刷新完成
     */
    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }
}
