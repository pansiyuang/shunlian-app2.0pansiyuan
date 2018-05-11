package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.StoreMsgEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 */

public interface IFoundCommentView extends IView {

    void getFoundCommentList(List<StoreMsgEntity.StoreMsg> list, int page, int totalPage);
}
