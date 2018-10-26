package com.shunlian.app.view;

import com.shunlian.app.bean.HotBlogsEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

public interface IAttentionMsgView extends IView {

    void getAttentionMsgList(List<HotBlogsEntity.MemberInfo> list,int page,int totalPage);

    void focusUser(int isFocus,String memberId);
}
