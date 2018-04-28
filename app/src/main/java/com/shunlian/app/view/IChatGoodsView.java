package com.shunlian.app.view;

import com.shunlian.app.newchat.entity.ChatGoodsEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public interface IChatGoodsView extends IView {

    void getChatGoodsList(List<ChatGoodsEntity.Goods> goodsList, int currentPage, int total);

    void refreshFinish();
}
