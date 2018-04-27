package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.MessageAdapter;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.presenter.CustomerPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ICustomerView;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CustomerListActivity extends BaseActivity implements ICustomerView, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.miv_distribution)
    MyImageView miv_distribution;

    @BindView(R.id.tv_change)
    TextView tv_change;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private CustomerPresenter mPresenter;
    private List<ChatMemberEntity.ChatMember> chatMemberList;
    private MessageAdapter mAdapter;
    private EasyWebsocketClient mClient;
    private UserInfoEntity.Info.User mUser;
    private int currentReception; //1表示工作中，0不在工作中
    private LinearLayoutManager manager;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, CustomerListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_customerlist;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.customer_list));

        if (EasyWebsocketClient.getClient() == null) {
            mClient = EasyWebsocketClient.initWebsocketClient(this);
        } else {
            mClient = EasyWebsocketClient.getClient();
        }
        chatMemberList = new ArrayList<>();
        mPresenter = new CustomerPresenter(this, this);

        if (mClient.getUser() != null) {
            mUser = mClient.getUser();
            mPresenter.getReception(mUser.user_id);
            mPresenter.getUserList(true, mUser.user_id);
        }

        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
    }

    @Override
    protected void initListener() {
        miv_distribution.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.miv_distribution:
                if (currentReception == 0) {
                    mPresenter.setReception(mUser.user_id, 1);
                } else {
                    mPresenter.setReception(mUser.user_id, 0);
                }
                break;
        }
        super.onClick(view);
    }

    @Override
    public void getUserList(List<ChatMemberEntity.ChatMember> member) {
        if (!isEmpty(member)) {
            chatMemberList.addAll(member);
        }
        if (mAdapter == null) {
            mAdapter = new MessageAdapter(this, null, chatMemberList);
            mAdapter.setOnItemClickListener(this);
            recycler_list.setAdapter(mAdapter);
        } else {
            mAdapter.notifyItemRangeInserted(0, chatMemberList.size());
        }
    }

    @Override
    public void getReception(int reception) {
        currentReception = reception;   //1表示工作中，0不在工作中
        setReceptionStatus(currentReception);
    }

    @Override
    public void setReception() {
        if (currentReception == 0) {
            currentReception = 1;
        } else {
            currentReception = 0;
        }
        setReceptionStatus(currentReception);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void setReceptionStatus(int reception) {
        switch (reception) {
            case 0:
                miv_distribution.setImageDrawable(getDrawableResouce(R.mipmap.icon_chat_userlist_h));
                break;
            case 1:
                miv_distribution.setImageDrawable(getDrawableResouce(R.mipmap.icon_chat_userlist_n));
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ChatMemberEntity.ChatMember chatMember = chatMemberList.get(position);
        ChatActivity.startAct(this, chatMember);
    }
}
