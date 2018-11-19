package com.shunlian.app.view;

import com.shunlian.app.bean.NoticeMsgEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/25.
 */

public interface INoticeMsgView extends IView {

    void getNoticeMsgList(List<NoticeMsgEntity.Notice> noticeList, int page, int totalPage);

    void refreshFinish();
}
