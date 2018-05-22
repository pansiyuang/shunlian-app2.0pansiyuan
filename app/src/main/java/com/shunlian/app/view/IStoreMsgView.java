package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.newchat.entity.StoreMessageEntity;
import com.shunlian.app.newchat.entity.StoreMsgEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 */

public interface IStoreMsgView extends IView {

    void getStoreMsg(StoreMessageEntity storeMessageEntity);
}
