package com.shunlian.app.ui.discover_new.discoverMsg;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DownloadMsgAdapter;
import com.shunlian.app.adapter.NoticeMsgAdapter;
import com.shunlian.app.bean.NoticeMsgEntity;
import com.shunlian.app.presenter.DownloadPresenter;
import com.shunlian.app.presenter.NoticeMsgPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.INoticeMsgView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class NoticeMsgFrag extends BaseLazyFragment implements INoticeMsgView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private NoticeMsgPresenter mPresenter;
    private LinearLayoutManager manager;
    private NoticeMsgAdapter mAdapter;
    private List<NoticeMsgEntity.Notice> notices;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.layout_refresh_list, null, false);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.initPage();
                    mPresenter.getNoticeMsgList(true);
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

        notices = new ArrayList<>();

        mPresenter = new NoticeMsgPresenter(getActivity(), this);
        mPresenter.getNoticeMsgList(true);

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText("还没有通知哦")
                .setButtonText(null);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getNoticeMsgList(List<NoticeMsgEntity.Notice> noticeList, int page, int totalPage) {
        refreshview.stopRefresh(true);
        if (page == 1) {
            notices.clear();
        }
        if (!isEmpty(noticeList)) {
            notices.addAll(noticeList);
        }

        if (page == 1 && isEmpty(notices)) {
            nei_empty.setVisibility(View.VISIBLE);
            recycler_list.setVisibility(View.GONE);
        } else {
            nei_empty.setVisibility(View.GONE);
            recycler_list.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            mAdapter = new NoticeMsgAdapter(getActivity(), notices);
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.setPageLoading(page, totalPage);
        mAdapter.notifyDataSetChanged();
    }
}
