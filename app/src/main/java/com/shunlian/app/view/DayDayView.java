package com.shunlian.app.view;

import com.shunlian.app.bean.ActivityListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface DayDayView extends IView {
    void getApiData(ActivityListEntity activityListEntity, int allPage, int page, List<ActivityListEntity.MData.Good.MList> list);
}
