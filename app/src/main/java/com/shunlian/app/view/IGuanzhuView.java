package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.ShareInfoParam;

/**
 * Created by Administrator on 2018/3/16.
 */

public interface IGuanzhuView extends IView {

    /**
     * 设置adapter
     * @param adapter
     */
    void setAdapter(BaseRecyclerAdapter adapter);

    /**
     * 刷新完成
     */
    void refreshFinish();

    /**
     * 分享
     * @param shareInfoParam
     */
    void share(ShareInfoParam shareInfoParam);
}
