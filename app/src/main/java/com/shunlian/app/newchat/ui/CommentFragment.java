package com.shunlian.app.newchat.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommentMsgAdapter;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.presenter.FoundCommentPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.view.IFoundCommentView;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/10.
 */

public class CommentFragment extends BaseFragment implements IFoundCommentView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    private FoundCommentPresenter mPresenter;
    private LinearLayoutManager manager;
    private List<StoreMsgEntity.StoreMsg> storeMsgList;
    private CommentMsgAdapter mAdapter;

    public static CommentFragment getInstance() {
        CommentFragment commentFragment = new CommentFragment();
        return commentFragment;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_refresh, container, false);
        return view;
    }

    @Override
    protected void initData() {
        mPresenter = new FoundCommentPresenter(getActivity(), this);
        mPresenter.getFoundCommentList(true);
        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        ((SimpleItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false);

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);

        storeMsgList = new ArrayList<>();
        mAdapter = new CommentMsgAdapter(getActivity(), storeMsgList);
        recycler_list.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.initPage();
                    mPresenter.getFoundCommentList(true);
                }
            }

            @Override
            public void onLoadMore() {

            }
        });
        super.initListener();
    }

    @Override
    public void getFoundCommentList(List<StoreMsgEntity.StoreMsg> list, int page, int totalPage) {
        if (refreshview != null) {
            refreshview.stopRefresh(true);
            if (page == 1) {
                storeMsgList.clear();
            }
            if (!isEmpty(list)) {
                storeMsgList.addAll(list);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
