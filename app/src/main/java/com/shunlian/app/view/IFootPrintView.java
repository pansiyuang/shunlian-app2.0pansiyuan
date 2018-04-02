package com.shunlian.app.view;

import com.shunlian.app.bean.CalendarEntity;
import com.shunlian.app.bean.FootprintEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */

public interface IFootPrintView extends IView {
    void getCalendarList(List<CalendarEntity> calendarEntityList);

    void getMarkList(List<FootprintEntity.MarkData> markDataList, List<FootprintEntity.DateInfo> dateInfoList, int page, int allPage);

    void delSuccess(String msg, List<FootprintEntity.DateInfo> dateInfoList);
}
