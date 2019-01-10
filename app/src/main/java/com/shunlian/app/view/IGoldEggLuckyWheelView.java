package com.shunlian.app.view;

import com.shunlian.app.bean.GoldEggPrizeEntity;
import com.shunlian.app.bean.TaskDrawEntity;

import java.util.List;

public interface IGoldEggLuckyWheelView  extends IView{

    void getPrizeData(GoldEggPrizeEntity goldEggPrizeEntity);

    void getTaskDraw(TaskDrawEntity taskDrawEntity);

    void getDrawRecordList(List<String> recordList);
}
