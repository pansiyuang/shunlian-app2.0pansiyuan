package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.MessageAdapter;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.MessageEntity;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.SystemMessageEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.util.SwitchStatusDialog;
import com.shunlian.app.newchat.util.TimeUtil;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MemberStatus;
import com.shunlian.app.presenter.MessagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IMessageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/26.
 */

public class SearchCustomerActivity extends BaseActivity implements IMessageView, EasyWebsocketClient.OnMessageReceiveListener, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.edt_customer_search)
    EditText edt_customer_search;

    @BindView(R.id.tv_search_cancel)
    TextView tv_search_cancel;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private MessagePresenter mPresenter;
    private MessageAdapter mAdapter;
    private LinearLayoutManager manager;
    private List<ChatMemberEntity.ChatMember> chatMemberList;
    private String currentKeyWord;
    private ObjectMapper mObjectMapper;
    private EasyWebsocketClient mClient;
    private MessageCountManager messageCountManager;
    private SwitchStatusDialog statusDialog;
    private MemberStatus mStatus;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SearchCustomerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_search_customer;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        messageCountManager = MessageCountManager.getInstance(this);

        mObjectMapper = new ObjectMapper();
        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
        chatMemberList = new ArrayList<>();
        mPresenter = new MessagePresenter(this, this);

        mClient = EasyWebsocketClient.getInstance(this);

        statusDialog = new SwitchStatusDialog(this).setOnButtonClickListener(new SwitchStatusDialog.OnButtonClickListener() {
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

    @Override
    protected void onResume() {
        if (mClient != null) {
            mClient.addOnMessageReceiveListener(this);
        }
        super.onResume();
    }

    @Override
    protected void initListener() {
        edt_customer_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                currentKeyWord = edt_customer_search.getText().toString().trim();
                mPresenter.getMessageList(true, currentKeyWord, "3");
            }
            return false;
        });
        tv_search_cancel.setOnClickListener(v -> finish());
        super.initListener();
    }

    @Override
    protected void onRestart() {
        LogUtil.httpLogW("onRestart()");
        messageCountManager.upDateMessageCount();
        resetData();
        super.onRestart();
    }

    public void resetData() {
        mPresenter.getMessageList(false, currentKeyWord, "3");
    }

    @Override
    public void getSysMessage(SystemMessageEntity systemMessageEntity) {

    }

    @Override
    public void getMessageList(List<ChatMemberEntity.ChatMember> members, int count) {
        if (!isEmpty(members)) {
            chatMemberList.clear();
            chatMemberList.addAll(members);
        }

        if (mAdapter == null) {
            mAdapter = new MessageAdapter(this, null, chatMemberList);
            mAdapter.setOnItemClickListener(this);
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void delSuccess(String msg) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

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
                if (baseMessage.from_user_id.equals(chatMemberList.get(i).m_user_id)) {
                    unReadNum = chatMemberList.get(i).unread_count + 1;
                    baseMessage.setuReadNum(unReadNum);
                    chatMemberList.get(i).unread_count = unReadNum;
                    chatMemberList.get(i).update_time = TimeUtil.getNewChatTime(msgInfo.send_time);
                    break;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
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

    }

    @Override
    public void transferMessage(String msg) {

    }

    @Override
    public void transferMemberAdd(String msg) {

    }

    @Override
    public void onLine() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void onItemClick(View view, int position) {
        if (!mClient.isMember()) {
            mStatus = MemberStatus.Member;
            statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Seller, MemberStatus.Member).show();
        } else {
            ChatMemberEntity.ChatMember chatMember = chatMemberList.get(position);
            ChatManager.getInstance(this).MemberChatToStore(chatMember);
        }
    }

    @Override
    protected void onStop() {
        mClient.removeOnMessageReceiveListener(this);
        super.onStop();
    }
}
