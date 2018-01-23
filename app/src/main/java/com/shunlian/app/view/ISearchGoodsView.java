package com.shunlian.app.view;

import com.shunlian.app.bean.HotSearchEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */

public interface ISearchGoodsView extends IView {
    void getSearchGoods(HotSearchEntity entity);

    void getSearchTips(List<String> tips);

    void clearSuccess(String str);
}
