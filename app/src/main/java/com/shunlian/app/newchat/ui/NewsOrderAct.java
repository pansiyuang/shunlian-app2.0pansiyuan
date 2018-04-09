package com.shunlian.app.newchat.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.newchat.adapter.ChatMemberAdapter;
import com.shunlian.app.newchat.entity.BaseEntity;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.StatusEntity;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.event.BaseEvent;
import com.shunlian.app.newchat.event.LineStatusEvent;
import com.shunlian.app.newchat.event.MessageRespEvent;
import com.shunlian.app.newchat.event.UpdateListEvent;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class NewsOrderAct extends BaseActivity {
    private ListView chatListView;
    //websocket客户端
    private EasyWebsocketClient mClient;
    private ChatMemberAdapter memberAdapter;
    private List<UserInfoEntity.Info.Friend> mList;
    private ObjectMapper objectMapper;
//    private ProgressDialog httpDialog;
    private MyImageView iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        chatListView = (ListView) findViewById(R.id.lv_chatlist);
        iv_back = (MyImageView) findViewById(R.id.iv_back);
        chatListView.setOnItemClickListener(l);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        EventBus.getDefault().register(this);
        objectMapper = new ObjectMapper();
        initChat();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initData() {

    }

    private void initChat() {

        if (EasyWebsocketClient.getClient() == null) {
//            httpDialog = new HttpDialog(this);
            mClient = EasyWebsocketClient.initWebsocketClient(this);
        } else {
            mClient = EasyWebsocketClient.getClient();
            if (mClient.getFriendList() != null) {
                mList = mClient.getFriendList();
                memberAdapter = new ChatMemberAdapter(chatListView, mList, NewsOrderAct.this);
                chatListView.setAdapter(memberAdapter);
            }
        }
    }

    private AdapterView.OnItemClickListener l = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (mList == null) {
                return;
            }
            UserInfoEntity.Info.Friend friend = mList.get(position);
            if (friend == null) {
                return;
            }
//            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
//            intent.putExtra("member", friend);
//            startActivity(intent);
        }
    };


    /**
     * 处理网络断开自动重连响应事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(BaseEvent event) {
        switch (event.getType()) {
            case MESSAGE:
                String msg = ((MessageRespEvent) event).getMessage();
                try {
                    BaseEntity baseEntity = objectMapper.readValue(msg, BaseEntity.class);
                    switch (baseEntity.message_type) {
                        case "init":
//                            if (httpDialog != null & httpDialog.isShowing()) {
//                                httpDialog.dismiss();
//                            }
                            if (mClient.getFriendList() != null) {
                                mList = mClient.getFriendList();
                                memberAdapter = new ChatMemberAdapter(chatListView, mList, NewsOrderAct.this);
                                chatListView.setAdapter(memberAdapter);
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case FRIENDLIST:
                BaseMessage baseMessage;
                String userId;
                List<UserInfoEntity.Info.Friend> friendList;
                if (((UpdateListEvent) event).getBaseMessage() != null) {
                    baseMessage = ((UpdateListEvent) event).getBaseMessage();
                    memberAdapter.updataItem(baseMessage);
                }
                userId = ((UpdateListEvent) event).getUserId();
                if (!TextUtils.isEmpty(userId)) {
                    memberAdapter.updateItemId(userId);
                }

                if ((((UpdateListEvent) event).getmList() != null)) {
                    friendList = ((UpdateListEvent) event).getmList();
                    mList = friendList;
                    memberAdapter.notifyDataSetChanged();
                }
                break;
            case LINESTATUS:
                //通知刷新用户在线状态
                StatusEntity statusEntity;
                if (((LineStatusEvent) event).getStatusEntity() != null) {
                    statusEntity = ((LineStatusEvent) event).getStatusEntity();
                    memberAdapter.updateItemStatus(statusEntity);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
