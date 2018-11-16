package com.shunlian.app.eventbus_bean;

import com.shunlian.app.bean.HotBlogsEntity;

/**
 * Created by Administrator on 2018/10/25.
 */

public class BaseInfoEvent {
    public HotBlogsEntity.BaseInfo baseInfo;

    public BaseInfoEvent(HotBlogsEntity.BaseInfo info) {
        this.baseInfo = info;
    }
}
