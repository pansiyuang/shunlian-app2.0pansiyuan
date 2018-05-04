package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.MessageListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */

public interface IMessageView extends IView {

    void getSysMessageList(List<MessageListEntity.Msg> msgList);

    void getMessageList(  List<ChatMemberEntity.ChatMember> members);

    void delSuccess(String msg);
}
