package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.StoreMsgEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 */

public interface IFoundTopicView extends IView {

    void getFoundTopicList(List<StoreMsgEntity.StoreMsg> list, int page, int totalPage);

}
