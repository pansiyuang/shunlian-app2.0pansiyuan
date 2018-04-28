package com.shunlian.app.view;


import com.shunlian.app.newchat.entity.ChatMemberEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public interface ICustomerView extends IView {

    void getUserList(List<ChatMemberEntity.ChatMember> member);

    void getReception(int reception);

    void setReception();
}
