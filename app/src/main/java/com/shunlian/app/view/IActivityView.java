package com.shunlian.app.view;

import com.shunlian.app.bean.DiscoverActivityEntity;

/**
 * Created by Administrator on 2018/10/18.
 */

public interface IActivityView extends IView {

    void getActivities(DiscoverActivityEntity activityEntity, int page, int totalPage);

    void refreshFinish();
}
