package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.newchat.adapter.TransferMemberAdapter;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.TransferOtherEntity;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.util.TransferDialog;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MemberStatus;
import com.shunlian.app.newchat.websocket.Status;
import com.shunlian.app.presenter.SwitchOtherPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ISwitchOtherView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/20.
 */

public class SwitchOtherActivity extends BaseActivity implements ISwitchOtherView, BaseRecyclerAdapter.OnItemClickListener, TransferDialog.OnReasonPutListener, EasyWebsocketClient.OnMessageReceiveListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private SwitchOtherPresenter mPresenter;
    private EasyWebsocketClient mClient;
    private UserInfoEntity.Info.User mUser;
    private List<ChatMemberEntity.ChatMember> chatMemberList;
    private String chatUserId;
    private String currentUserId;
    private String currentServiceNum;
    private String currentServiceId;
    private TransferMemberAdapter mAdapter;
    private ChatMemberEntity.ChatMember currentChatMember;
    private TransferDialog transferDialog;
    private ObjectMapper objectMapper;

    public static void startAct(Context context, String mUserId, String chatUserId, String serviceId) {
        Intent intent = new Intent(context, SwitchOtherActivity.class);
        intent.putExtra("chat_user_id", chatUserId);
        intent.putExtra("user_id", mUserId);
        intent.putExtra("service_id", serviceId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_switch_other;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.choose_customer_list));
        chatUserId = getIntent().getStringExtra("chat_user_id");
        currentUserId = getIntent().getStringExtra("user_id");
        currentServiceId = getIntent().getStringExtra("service_id");

        objectMapper = new ObjectMapper();

        tv_title_right.setText(getString(R.string.next_step));
        tv_title_right.setVisibility(View.VISIBLE);

        mClient = EasyWebsocketClient.getInstance(this);
        mClient.addOnMessageReceiveListener(this);

        mPresenter = new SwitchOtherPresenter(this, this);
        if (mClient.getUser() != null) {
            mUser = mClient.getUser();
            if (mClient.getMemberStatus() == MemberStatus.Admin) {
                mPresenter.getTransferChatUserList("1", "", chatUserId);
            } else if (mClient.getMemberStatus() == MemberStatus.Seller) {
                mPresenter.getTransferChatUserList("2", mUser.shop_id, chatUserId);
            }
        }

        chatMemberList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
    }

    @Override
    protected void initListener() {
        tv_title_right.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getMemberList(List<ChatMemberEntity.ChatMember> memberList, String serviceNum) {
        if (!isEmpty(memberList)) {
            chatMemberList.addAll(memberList);
        }
        currentServiceNum = serviceNum;

        if (mAdapter == null) {
            mAdapter = new TransferMemberAdapter(this, chatMemberList);
            mAdapter.setOnItemClickListener(this);
            recycler_list.setAdapter(mAdapter);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        boolean isSelect = chatMemberList.get(position).isSelect;
        if (isSelect) {
            chatMemberList.get(position).isSelect = false;
            currentChatMember = null;
        } else {
            for (ChatMemberEntity.ChatMember chatMember : chatMemberList) {
                chatMember.isSelect = false;
            }
            chatMemberList.get(position).isSelect = true;
            currentChatMember = chatMemberList.get(position);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (currentChatMember != null) {
            if (transferDialog == null) {
                transferDialog = new TransferDialog(this);
                transferDialog.setOnReasonPutListener(this);
            }
            transferDialog.show();
        } else {
            Common.staticToast("请选择要转接的客服");
        }
        super.onClick(view);
    }

    @Override
    public void OnSubmit(String reason) {
        transferCustomer(reason);
    }

    /**
     * 转接客户
     */
    public void transferCustomer(String reason) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "zhuanjie_service");
            jsonObject.put("sid", currentServiceId);//服务编号
            jsonObject.put("kidz", mUser.user_id); //转接者聊天用户编号
            jsonObject.put("kidj", currentChatMember.user_id);//接受转接客服的聊天用户编号
            jsonObject.put("user_id", chatUserId);//被转接的用户的用户编号
            jsonObject.put("item", reason);//转接备注

            if (mClient.getStatus() == Status.CONNECTED) {
                LogUtil.httpLogW("转接的json:" + jsonObject.toString());
                mClient.send(jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mClient.removeOnMessageReceiveListener(this);
        super.onDestroy();
    }

    @Override
    public void initMessage() {

    }

    @Override
    public void receiveMessage(String msg) {
    }

    @Override
    public void evaluateMessage(String msg) {

    }

    @Override
    public void roleSwitchMessage(String msg) {

    }

    @Override
    public void transferMessage(String msg) {
        try {
            TransferOtherEntity transferMessage = objectMapper.readValue(msg, TransferOtherEntity.class);
            switch (transferMessage.status) {
                case 0://成功
                    CustomerListActivity.startAct(SwitchOtherActivity.this);
                    break;
            }
            new Thread() {
                public void run() {
                    Looper.prepare();
                    Toast.makeText(SwitchOtherActivity.this, transferMessage.msg, Toast.LENGTH_SHORT).show();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
