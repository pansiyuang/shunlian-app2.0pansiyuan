package com.shunlian.app.newchat.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MessageAdapter;
import com.shunlian.app.newchat.adapter.TopMessageAdapter;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.newchat.entity.StoreMessageEntity;
import com.shunlian.app.presenter.StoreMsgPresenter;
import com.shunlian.app.presenter.StorePresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.IStoreMsgView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/8.
 */

public class LittleStoreFragment extends BaseLazyFragment implements IStoreMsgView, TopMessageAdapter.OnMessageClickListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private List<MessageListEntity.Msg> msgList;
    private TopMessageAdapter mAdapter;
    private StoreMsgPresenter storeMsgPresenter;

    public static LittleStoreFragment getInstance() {
        LittleStoreFragment littleStoreFragment = new LittleStoreFragment();
        return littleStoreFragment;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_list, container, false);
        return view;
    }

    @Override
    protected void initData() {
        msgList = new ArrayList<>();
        mAdapter = new TopMessageAdapter(getActivity(), msgList);
        storeMsgPresenter = new StoreMsgPresenter(getActivity(), this);

        recycler_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setAdapter(mAdapter);

        storeMsgPresenter.getStoreMessage();

        mAdapter.setOnMessageClickListener(this);
    }

    @Override
    public void getStoreMsg(StoreMessageEntity storeMessageEntity) {
        msgList.clear();
        if (storeMessageEntity.memberAdd != null) {
            MessageListEntity.Msg msg = new MessageListEntity.Msg();
            msg.type = "6";
            msg.title = storeMessageEntity.memberAdd.title;
            msgList.add(msg);
        }
        if (storeMessageEntity.shopping != null) {
            MessageListEntity.Msg msg = new MessageListEntity.Msg();
            msg.type = "7";
            msg.title = storeMessageEntity.shopping.title;
            msgList.add(msg);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void OnSysMsgClick() {

    }

    @Override
    public void OnTopMsgClick() {

    }

    @Override
    public void OnAdminMsgClick() {

    }

    @Override
    public void OnSellerMsgClick() {

    }

    @Override
    public void OnOrderMsgClick() {
        StoreMsgActivity.startAct(getActivity(), false);
    }

    @Override
    public void OnStoreMsgClick() {
        StoreMsgActivity.startAct(getActivity(), true);
    }

}
