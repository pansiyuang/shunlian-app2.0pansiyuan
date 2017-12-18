package com.shunlian.app.view;

import com.shunlian.app.bean.MyOrderEntity;


/**
 * Created by Administrator on 2017/12/16.
 */

public interface ISearchResultView extends IView {

    void getSearchResult(MyOrderEntity myOrderEntity);
}
