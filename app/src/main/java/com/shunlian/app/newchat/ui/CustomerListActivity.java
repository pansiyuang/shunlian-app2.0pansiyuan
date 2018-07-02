package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.MessageAdapter;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.MessageEntity;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.SwitchStatusEntity;
import com.shunlian.app.newchat.entity.TransferMemberEntity;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.TimeUtil;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MemberStatus;
import com.shunlian.app.newchat.websocket.Status;
import com.shunlian.app.presenter.CustomerPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICustomerView;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CustomerListActivity extends BaseActivity implements ICustomerView, BaseRecyclerAdapter.OnItemClickListener, EasyWebsocketClient.OnMessageReceiveListener {
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
    private ObjectMapper mObjectMapper;

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
        mObjectMapper = new ObjectMapper();

        mClient = EasyWebsocketClient.getInstance(this);
        chatMemberList = new ArrayList<>();
        mPresenter = new CustomerPresenter(this, this);

        if (mClient.getUser() != null) {
            mUser = mClient.getUser();
            mPresenter.getReception(mUser.user_id);
            mPresenter.getUserList(true, mUser.user_id);

            mClient.addOnMessageReceiveListener(this);
        }

        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);

    }

    @Override
    protected void initListener() {
        miv_distribution.setOnClickListener(this);
        tv_change.setOnClickListener(this);
        super.initListener();
    }

    @Override
    protected void onRestart() {
        resetData();
        super.onRestart();
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
            case R.id.tv_change:
                if (mClient.getStatus() == Status.CONNECTED) {
                    mClient.switchStatus(MemberStatus.Member);
                }
                break;
        }
        super.onClick(view);
    }

    @Override
    public void getUserList(List<ChatMemberEntity.ChatMember> member) {
        chatMemberList.clear();
        if (!isEmpty(member)) {
            chatMemberList.addAll(member);
        }
        if (mAdapter == null) {
            mAdapter = new MessageAdapter(this, null, chatMemberList);
            mAdapter.setOnItemClickListener(this);
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
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
                miv_distribution.setImageDrawable(getDrawableResouce(R.mipmap.icon_chat_userlist_n));
                break;
            case 1:
                miv_distribution.setImageDrawable(getDrawableResouce(R.mipmap.icon_chat_userlist_h));
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ChatMemberEntity.ChatMember chatMember = chatMemberList.get(position);
        ChatManager.getInstance(this).init().StoreChatToMember(chatMember);
    }

    public void resetData() {
        mPresenter.getUserList(false, mUser.user_id);
    }

    @Override
    public void initMessage() {

    }

    @Override
    public void receiveMessage(String msg) {
        updateFriendList(msg);
    }

    @Override
    public void evaluateMessage(String msg) {
//        updateFriendList(msg);
    }

    @Override
    public void roleSwitchMessage(String msg) {
        try {
            SwitchStatusEntity switchStatusEntity = mObjectMapper.readValue(msg, SwitchStatusEntity.class);
            if (switchStatusEntity.status.equals("0")) {
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(CustomerListActivity.this, switchStatusEntity.msg, Toast.LENGTH_SHORT).show();
                        finish();
                        Looper.loop();// 进入loop中的循环，查看消息队列
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transferMessage(String msg) {

    }

    @Override
    public void transferMemberAdd(String msg) {
        LogUtil.httpLogW("收到转接用户刷新界面啦");
        try {
            TransferMemberEntity memberEntity = mObjectMapper.readValue(msg, TransferMemberEntity.class);
            ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();

            chatMember.user_id = memberEntity.user_id;
            chatMember.headurl = memberEntity.headurl;
            chatMember.nickname = memberEntity.nickname;
            chatMember.update_time = memberEntity.re_time;
            chatMember.sid = memberEntity.sid;
            chatMember.update_time = memberEntity.q_time;
            chatMember.line_status = memberEntity.line_status;
            chatMember.type = "0";

            if (chatMemberList.size() != 0) {
                for (int i = 0; i < chatMemberList.size(); i++) {
                    ChatMemberEntity.ChatMember member = chatMemberList.get(i);
                    if (member.user_id.equals(memberEntity.user_id)) { //
                        chatMemberList.remove(member);
                        break;
                    }
                }
                chatMemberList.add(0, chatMember);
                LogUtil.httpLogW("列表不为空");
            } else {
                chatMemberList.add(chatMember);
                LogUtil.httpLogW("列表为空");
            }
            runOnUiThread(() -> mAdapter.notifyDataSetChanged());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLine() {

    }

    @Override
    public void logout() {

    }

    public void updateFriendList(String message) {
        BaseMessage baseMessage = null;
        MsgInfo msgInfo = null;
        try {
            MessageEntity messageEntity = mObjectMapper.readValue(message, MessageEntity.class);
            msgInfo = messageEntity.msg_info;
            String message1 = msgInfo.message;
            baseMessage = mObjectMapper.readValue(message1, BaseMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int unReadNum;
        if (!isEmpty(chatMemberList)) {
            for (int i = 0; i < chatMemberList.size(); i++) {
                //好友发给自己的消息
                if (baseMessage.from_user_id.equals(chatMemberList.get(i).user_id)) {
                    unReadNum = chatMemberList.get(i).unread_count + 1;
                    baseMessage.setuReadNum(unReadNum);
                    chatMemberList.get(i).unread_count = unReadNum;
                    chatMemberList.get(i).update_time = TimeUtil.getNewChatTime(msgInfo.send_time);
                    break;
                }
            }
        }
        runOnUiThread(() -> {
            mAdapter.notifyDataSetChanged();
        });
    }
}
