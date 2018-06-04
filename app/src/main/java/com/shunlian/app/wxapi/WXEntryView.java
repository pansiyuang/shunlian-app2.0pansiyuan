package com.shunlian.app.wxapi;

import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.view.IView;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface WXEntryView extends IView {

    void onWXCallback(WXLoginEntity wxLoginEntity);
    void notifyCallback(CommonEntity commonEntity);
}
