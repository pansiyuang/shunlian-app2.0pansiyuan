package com.shunlian.app.newchat.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MessageAdapter;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.presenter.MessagePresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.IMessageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MessageListFragment extends BaseLazyFragment implements IMessageView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private MessagePresenter mPresenter;
    private List<MessageListEntity.Msg> msgs;
    private MessageAdapter mAdapter;
    private LinearLayoutManager manager;

    public static MessageListFragment getInstance() {
        MessageListFragment messageListFragment = new MessageListFragment();
        return messageListFragment;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_list, container, false);
        return view;
    }

    @Override
    protected void initData() {
        mPresenter = new MessagePresenter(getActivity(), this);
        mPresenter.getSystemMessage();
        msgs = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
    }

    @Override
    public void getSysMessageList(List<MessageListEntity.Msg> msgList) {
        if (!isEmpty(msgList)) {
            msgs.addAll(msgList);
        }
        if (mAdapter == null) {
            mAdapter = new MessageAdapter(getActivity(), msgs);
            recycler_list.setAdapter(mAdapter);
        } else {
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
