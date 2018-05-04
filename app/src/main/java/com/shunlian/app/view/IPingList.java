package com.shunlian.app.view;

import com.shunlian.app.bean.CorePingEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IPingList extends IView {
    void setApiData(CorePingEntity corePingEntity, List<CorePingEntity.MData> mDatas);
}
