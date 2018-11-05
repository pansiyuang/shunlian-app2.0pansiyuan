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
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
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

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

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

        NestedSlHeader header = new NestedSlHeader(baseContext);
        lay_refresh.setRefreshHeaderView(header);
        recycler_list.setNestedScrollingEnabled(false);

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        memberInfoList = new ArrayList<>();

        mPresenter = new AttentionMsgPresenter(getActivity(), this);
        mPresenter.getAttentionMsgList(true);

        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText("还没有人关注你哦")
                .setButtonText(null);
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                mPresenter.initPage();
                mPresenter.getAttentionMsgList(true);
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
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getAttentionMsgList(List<HotBlogsEntity.MemberInfo> list, int page, int totalPage) {
        if (page == 1) {
            memberInfoList.clear();
        }
        if (!isEmpty(list)) {
            memberInfoList.addAll(list);
        }

        if (page == 1 && isEmpty(memberInfoList)) {
            nei_empty.setVisibility(View.VISIBLE);
            lay_refresh.setVisibility(View.GONE);
        } else {
            nei_empty.setVisibility(View.GONE);
            lay_refresh.setVisibility(View.VISIBLE);
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
