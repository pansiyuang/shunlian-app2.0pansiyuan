package com.shunlian.app.wxapi;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.view.IView;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface WXEntryView extends IView {

    void onWXCallback(BaseEntity<WXLoginEntity> wxLoginEntity);
    void notifyCallback(CommonEntity commonEntity);

    /**
     * 金蛋数量
     * @param format
     */
    default void golde_eggs(String format){}

    /**
     * 分享失败关闭页面
     */
    default void cloasePage(){}
}
