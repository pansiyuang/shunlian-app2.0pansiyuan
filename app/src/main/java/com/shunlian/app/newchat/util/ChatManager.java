package com.shunlian.app.newchat.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.shunlian.app.App;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.ui.ChatActivity;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MemberStatus;
import com.shunlian.app.newchat.websocket.Status;
import com.shunlian.app.utils.LogUtil;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ChatManager {

    public static Context mContext;
    public static ChatManager chatManager;
    private SwitchStatusDialog statusDialog;
    private EasyWebsocketClient mClient;
    private MemberStatus mStatus;
    private ChatMemberEntity.ChatMember currentChatMember;
    private boolean isPush = false;

    public static ChatManager getInstance(Context context) {
        mContext = context;
        if (chatManager == null) {
            synchronized (ChatManager.class) {
                if (chatManager == null) {
                    chatManager = new ChatManager();
                }
            }
        }
        return chatManager;
    }

    public ChatManager init() {
        mClient = EasyWebsocketClient.getInstance(mContext);
        statusDialog = new SwitchStatusDialog(mContext).setOnButtonClickListener(new SwitchStatusDialog.OnButtonClickListener() {
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
            if (isPush) {
                ChatActivity.startAct(mContext, currentChatMember);
            }
        });
        return this;
    }

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
            ChatActivity.startAct(mContext, chatMember, goodsDeatilEntity);
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
            ChatActivity.startAct(mContext, chatMember);
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
            ChatActivity.startAct(mContext, chatMember);
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
            ChatActivity.startAct(mContext, chatMember);
        }
    }


    public void switch2jumpChat(String fromType, String toType, ChatMemberEntity.ChatMember chatMember) {
        if (TextUtils.isEmpty(fromType) || TextUtils.isEmpty(toType) || chatMember == null) {
            return;
        }
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
                            ChatActivity.startAct(mContext, currentChatMember);
                        }
                        break;
                    case "3":
                    case "4":
                        if (!mClient.isSeller()) {
                            mClient.switchStatus(MemberStatus.Seller);
                        } else {
                            ChatActivity.startAct(mContext, currentChatMember);
                        }
                        break;
                }
                break;
            case "1":
            case "2":
                if (!mClient.isMember()) {
                    mClient.switchStatus(MemberStatus.Member);
                } else {
                    ChatActivity.startAct(mContext, currentChatMember);
                }
                break;
            case "3":
            case "4":
                if (!mClient.isMember()) {
                    mClient.switchStatus(MemberStatus.Member);
                } else {
                    ChatActivity.startAct(mContext, currentChatMember);
                }
                break;
        }
    }
}
