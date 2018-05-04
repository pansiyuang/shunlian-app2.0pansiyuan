package com.shunlian.app.newchat.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.MessageAdapter;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.MessageEntity;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.util.SwitchStatusDialog;
import com.shunlian.app.newchat.util.TimeUtil;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MemberStatus;
import com.shunlian.app.presenter.MessagePresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.ui.message.SystemMsgAct;
import com.shunlian.app.view.IMessageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MessageListFragment extends BaseLazyFragment implements IMessageView, BaseRecyclerAdapter.OnItemClickListener, MessageAdapter.OnStatusClickListener, EasyWebsocketClient.OnMessageReceiveListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private static MessagePresenter mPresenter;
    private List<MessageListEntity.Msg> msgs;
    private List<ChatMemberEntity.ChatMember> memberList;
    private MessageAdapter mAdapter;
    private LinearLayoutManager manager;
    private SwitchStatusDialog statusDialog;
    private EasyWebsocketClient mClient;
    private MemberStatus mStatus;
    private ObjectMapper mObjectMapper;
    private UserInfoEntity.Info.User mUser;

    public static MessageListFragment getInstance() {
        MessageListFragment messageListFragment = new MessageListFragment();
        return messageListFragment;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_list, container, false);
        mObjectMapper = new ObjectMapper();
        return view;
    }

    @Override
    protected void initData() {
        mPresenter = new MessagePresenter(getActivity(), this);
        mPresenter.getSystemMessage();
        mPresenter.getMessageList(true, "", "");
        msgs = new ArrayList<>();
        memberList = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity());
        mAdapter = new MessageAdapter(getActivity(), msgs, memberList);
        recycler_list.setLayoutManager(manager);
        recycler_list.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnStatusClickListener(this);

        if (EasyWebsocketClient.getClient() == null) {
            mClient = EasyWebsocketClient.initWebsocketClient(getActivity());
        } else {
            mClient = EasyWebsocketClient.getClient();
        }

        if (mClient != null) {
            if (mClient.getUser() != null) {
                mUser = mClient.getUser();
            }
            mClient.addOnMessageReceiveListener(this);
        }

        statusDialog = new SwitchStatusDialog(getActivity()).setOnButtonClickListener(new SwitchStatusDialog.OnButtonClickListener() {
            @Override
            public void OnClickSure() {
                mClient.switchStatus(mStatus);
                statusDialog.dismiss();
            }

            @Override
            public void OnClickCancle() {
                statusDialog.dismiss();
            }
        });
    }

    public void resetData() {
        mPresenter.getSystemMessage();
        mPresenter.getMessageList(false, "", "");
    }

    @Override
    public void getSysMessageList(List<MessageListEntity.Msg> msgList) {
        if (!isEmpty(msgList)) {
            msgs.clear();
            msgs.addAll(msgList);

            if (mClient.isBindAdmin()) { //平台客服
                MessageListEntity.Msg msg = new MessageListEntity.Msg();
                msg.type = "1";
                msgs.add(msg);
            }
            if (mClient.isBindSeller()) { //商家客服
                MessageListEntity.Msg msg = new MessageListEntity.Msg();
                msg.type = "2";
                msgs.add(msg);
            }
        }
    }

    @Override
    public void getMessageList(List<ChatMemberEntity.ChatMember> members) {
        if (!isEmpty(members)) {
            memberList.clear();
            memberList.addAll(members);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void delSuccess(String msg) {

    }

    public void updateFriendList(String message) {
        BaseMessage baseMessage = null;
        try {
            MessageEntity messageEntity = mObjectMapper.readValue(message, MessageEntity.class);
            MsgInfo msgInfo = messageEntity.msg_info;
            String message1 = msgInfo.message;
            baseMessage = mObjectMapper.readValue(message1, BaseMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int unReadNum;
        if (!isEmpty(memberList)) {
            for (int i = 0; i < memberList.size(); i++) {
                //好友发给自己的消息
                if (baseMessage.from_user_id.equals(memberList.get(i).m_user_id)) {
                    unReadNum = memberList.get(i).unread_count + 1;
                    baseMessage.setuReadNum(unReadNum);
                    memberList.get(i).unread_count = unReadNum;
                    memberList.get(i).update_time = TimeUtil.getNewChatTime(baseMessage.sendTime / 1000);
                    break;
                }
            }
        }
        getActivity().runOnUiThread(() -> {
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onItemClick(View view, int position) {
        if (!isEmpty(msgs)) {
            if (!mClient.isMember()) {
                mStatus = MemberStatus.Member;
                statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Seller, MemberStatus.Member).show();
            } else {
                ChatMemberEntity.ChatMember member = memberList.get(position - 1);
                ChatActivity.startAct(getActivity(), member);
            }
        }
    }

    @Override
    public void OnSysClick() {
        //系统消息点击
        SystemMsgAct.startAct(baseContext);
    }

    @Override
    public void OnTopicClick() {
        //头条消息点击
    }

    @Override
    public void OnSellerClick() {
        if (!mClient.isSeller()) {
            mStatus = MemberStatus.Seller;
            statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Member, MemberStatus.Seller).show();
        } else {
            CustomerListActivity.startAct(getActivity());
        }
    }

    @Override
    public void OnAdminClick() {
        if (!mClient.isAdmin()) {
            mStatus = MemberStatus.Admin;
            statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Member, MemberStatus.Admin).show();
        } else {
            CustomerListActivity.startAct(getActivity());
        }
    }

    @Override
    public void OnMessageDel(String userId) {
        mPresenter.deleteMessage(userId);
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
        updateFriendList(msg);
    }

    @Override
    public void roleSwitchMessage(String msg) {
    }

    @Override
    public void onLine() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void onDestroy() {
        mClient.removeOnMessageReceiveListener(this);
        super.onDestroy();
    }
}
