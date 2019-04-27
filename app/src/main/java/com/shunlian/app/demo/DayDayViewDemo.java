package com.shunlian.app.demo;

import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.view.IView;

import java.util.List;

/**
 * Created by Administrator on 2019/3/27.
 */

public interface DayDayViewDemo extends IView {
    void getApiData(ActivityListEntity activityListEntity, int allPage, int page, List<ActivityListEntity.MData.Good.MList> list);
    void activityState(int position);
    void initMenu(List<ActivityListEntity.Menu> menus);
}
