package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.StoreMsgEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 */

public interface IVipMsgView extends IView {

    void getStoreMsgs(List<StoreMsgEntity.StoreMsg> msgList, int page, int total);

    void getOrderMsgs(List<StoreMsgEntity.StoreMsg> msgList, int page, int total);
}
