package com.shunlian.app.view;

import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

public interface IActivityDetailView extends IView {
    void getActivityDetail(List<BigImgEntity.Blog> blogList, HotBlogsEntity.Detail detail, int page, int totalPage);

    void focusUser(int isFocus,String memberId);

    void downCountSuccess(String blogId);

    void praiseBlog(String blogId);

    void shareGoodsSuccess(String blogId,String goodsId);

    void refreshFinish();
}
