package com.shunlian.app.newchat.util;

import android.content.Context;

import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.ui.ChatActivity;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MemberStatus;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ChatManager {

    public static Context mContext;
    public static ChatManager chatManager;
    private SwitchStatusDialog statusDialog;
    private EasyWebsocketClient mClient;
    private MemberStatus mStatus;

    public static ChatManager getInstance(Context context) {
        mContext = context;
        if (chatManager == null) {
            synchronized (MessageCountManager.class) {
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
        return this;
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
        if (!mClient.isMember()) {
            mStatus = MemberStatus.Seller;
            statusDialog.setDialogMessage(mClient.getMemberStatus(), MemberStatus.Admin, MemberStatus.Member).show();
        } else {
            ChatActivity.startAct(mContext, chatMember);
        }
    }
}
