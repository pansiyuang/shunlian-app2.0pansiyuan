package com.shunlian.app.view;

import com.shunlian.app.bean.WeekExpertEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/19.
 */

public interface IWeekExpertView extends IView {

    void expertList(List<WeekExpertEntity.Expert> expertList);

    void focusUser(int isFocus, String memberId);
}
