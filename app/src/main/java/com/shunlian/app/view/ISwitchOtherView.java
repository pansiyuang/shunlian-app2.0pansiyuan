package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.ChatMemberEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface ISwitchOtherView extends IView {

    void getMemberList(List<ChatMemberEntity.ChatMember> memberList,String serviceNum);
}
