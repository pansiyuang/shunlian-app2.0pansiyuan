package com.shunlian.app.newchat.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.ui.ChatActivity;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MemberStatus;
import com.shunlian.app.newchat.websocket.Status;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.HttpDialog;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ChatManager {

    private static Context mContext;
    public static ChatManager chatManager;
    private SwitchStatusDialog statusDialog;
    private EasyWebsocketClient mClient;
    private MemberStatus mStatus;
    private ChatMemberEntity.ChatMember currentChatMember;
    private boolean isPush = false;
    private HttpDialog httpDialog;

    private ChatManager() {
    }

    public static ChatManager getInstance(Context context) {
        if (chatManager == null) {
            synchronized (ChatManager.class) {
                if (chatManager == null) {
                    chatManager = new ChatManager();
                }
            }
        }
        mContext = context;
        return chatManager;
    }

    public ChatManager init() {
        mClient = EasyWebsocketClient.getInstance(mContext);
        httpDialog = new HttpDialog(mContext);
        statusDialog = new SwitchStatusDialog((Activity) mContext).setOnButtonClickListener(new SwitchStatusDialog.OnButtonClickListener() {
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
        mClient.setOnSwitchStatusListener(roleType -> {
            LogUtil.httpLogW("isPush:" + isPush);
            if (isPush) {
                startToChat(currentChatMember);
                resetPushMode();
            }
        });
        return this;
    }

    //切换身份跳转聊天
    public void resetPushMode() {
        isPush = false;
    }

    /**
     * 用户和商家聊天
     */
    public void MemberChatToStore(ChatMemberEntity.ChatMember chatMember) {
        MemberChatToStore(chatMember, null);
    }

    /**
     * 用户和商家聊天
     */
    public void MemberChatToStore(ChatMemberEntity.ChatMember chatMember, GoodsDeatilEntity goodsDeatilEntity) {
        isPush = false;
        if (!mClient.isMember()) {
            mStatus = MemberStatus.Member;
            statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Seller, MemberStatus.Member).show();
        } else {
            startToChat(chatMember, goodsDeatilEntity);
        }
    }

    /**
     * 平台和成员聊天
     */
    public void PlatformChatToMember(ChatMemberEntity.ChatMember chatMember) {
        if (!mClient.isAdmin()) {
            mStatus = MemberStatus.Admin;
            statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Member, MemberStatus.Admin).show();
        } else {
            startToChat(chatMember);
        }
    }

    /**
     * 商家和成员聊天
     */
    public void StoreChatToMember(ChatMemberEntity.ChatMember chatMember) {
        if (!mClient.isSeller()) {
            mStatus = MemberStatus.Seller;
            statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Member, MemberStatus.Seller).show();
        } else {
            startToChat(chatMember);
        }
    }

    /**
     * 用户和平台聊天
     */
    public void MemberChat2Platform(ChatMemberEntity.ChatMember chatMember) {
        isPush = false;
        if (!mClient.isMember()) {
            mStatus = MemberStatus.Seller;
            statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Admin, MemberStatus.Member).show();
        } else {
            startToChat(chatMember);
        }
    }


    public void switch2jumpChat(String fromType, String toType, ChatMemberEntity.ChatMember chatMember) {
        if (TextUtils.isEmpty(fromType) || TextUtils.isEmpty(toType) || chatMember == null) {
            return;
        }
        LogUtil.httpLogW("switch2jumpChat");
        isPush = true;
        currentChatMember = chatMember;
        switch (fromType) {
            case "0":
                switch (toType) {
                    case "1":
                    case "2":
                        if (!mClient.isAdmin()) {
                            mClient.switchStatus(MemberStatus.Admin);
                        } else {
                            startToChat(currentChatMember);
                        }
                        break;
                    case "3":
                    case "4":
                        if (!mClient.isSeller()) {
                            mClient.switchStatus(MemberStatus.Seller);
                        } else {
                            startToChat(currentChatMember);
                        }
                        break;
                }
                break;
            case "1":
            case "2":
                if (!mClient.isMember()) {
                    mClient.switchStatus(MemberStatus.Member);
                } else {
                    startToChat(currentChatMember);
                }
                break;
            case "3":
            case "4":
                if (!mClient.isMember()) {
                    mClient.switchStatus(MemberStatus.Member);
                } else {
                    startToChat(currentChatMember);
                }
                break;
        }
    }

    public void startToChat(ChatMemberEntity.ChatMember chatMember, GoodsDeatilEntity goodsDeatilEntity) {
        if (mClient.getStatus() == Status.CONNECTED) {
            ChatActivity.startAct(mContext, chatMember, goodsDeatilEntity);
        } else {
            if (httpDialog.isShowing()) {
                return;
            }
            httpDialog.show();
            mClient.buildeWebsocketClient();
            mClient.setOnConnetListener(() -> {
                httpDialog.dismiss();
                ChatActivity.startAct(mContext, chatMember, goodsDeatilEntity);
            });
        }
    }

    public void startToChat(ChatMemberEntity.ChatMember chatMember) {
        if (mClient.getStatus() == Status.CONNECTED) {
            ChatActivity.startAct(mContext, chatMember);
        } else {
            if (httpDialog.isShowing()) {
                return;
            }
            httpDialog.show();
            mClient.buildeWebsocketClient();
            mClient.setOnConnetListener(() -> {
                httpDialog.dismiss();
                ChatActivity.startAct(mContext, chatMember);
            });
        }
    }
}
