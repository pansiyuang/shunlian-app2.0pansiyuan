package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.ReplysetEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public interface IChatInputView extends IView {

    void getReplysetList(List<ReplysetEntity.Replyset> replysetList);
}
