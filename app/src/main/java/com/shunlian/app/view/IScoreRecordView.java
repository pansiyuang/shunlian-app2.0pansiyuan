package com.shunlian.app.view;

import com.shunlian.app.bean.CreditLogEntity;

import java.util.List;

public interface IScoreRecordView extends IView {

    void getScoreRecord(List<CreditLogEntity.CreditLog> creditLogList, int page, int totalPage);

    void refreshFinish();
}
