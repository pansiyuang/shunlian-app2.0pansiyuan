package com.shunlian.app.view;

import com.shunlian.app.bean.SaturdayDrawRecordEntity;

import java.util.List;

public interface IDrawRecordView extends IView {

    void getDrawRecord(List<SaturdayDrawRecordEntity.SaturdayDrawRecord> drawRecordList,int page,int total_page);
}
