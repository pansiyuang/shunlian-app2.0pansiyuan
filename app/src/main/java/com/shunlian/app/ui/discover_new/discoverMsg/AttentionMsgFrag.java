package com.shunlian.app.ui.discover_new.discoverMsg;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AttentionMsgAdapter;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.presenter.AttentionMsgPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.IAttentionMsgView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class AttentionMsgFrag extends BaseLazyFragment implements IAttentionMsgView, AttentionMsgAdapter.OnAdapterCallBack {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private AttentionMsgPresenter mPresenter;
    private LinearLayoutManager manager;
    private AttentionMsgAdapter mAdapter;
    private List<HotBlogsEntity.MemberInfo> memberInfoList;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_attention_msg, null, false);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);
        recycler_list.setNestedScrollingEnabled(false);

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        memberInfoList = new ArrayList<>();

        mPresenter = new AttentionMsgPresenter(getActivity(), this);
        mPresenter.getAttentionMsgList(true);
    }

    @Override
    protected void initListener() {
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.initPage();
                    mPresenter.getAttentionMsgList(true);
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
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getAttentionMsgList(List<HotBlogsEntity.MemberInfo> list, int page, int totalPage) {
        refreshview.stopRefresh(true);
        if (page == 1) {
            memberInfoList.clear();
        }
        if (!isEmpty(list)) {
            memberInfoList.addAll(list);
        }
        if (mAdapter == null) {
            mAdapter = new AttentionMsgAdapter(getActivity(), memberInfoList);
            mAdapter.setAdapterCallBack(this);
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.setPageLoading(page, totalPage);
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        for (HotBlogsEntity.MemberInfo memberInfo : memberInfoList) {
            if (memberId.equals(memberInfo.member_id)) {
                if (memberInfo.is_fans == 0) {
                    memberInfo.is_fans = 1;
                } else {
                    memberInfo.is_fans = 0;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void toFocusUser(int isFocus, String memberId) {
        mPresenter.focusUser(isFocus, memberId);
    }
}
