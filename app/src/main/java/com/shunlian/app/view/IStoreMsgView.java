package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.MessageListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 */

public interface IStoreMsgView extends IView {

    void getStoreMsgList(List<MessageListEntity.Msg> msgList);
}
