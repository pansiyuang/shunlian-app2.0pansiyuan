package com.shunlian.app.view;

import com.shunlian.app.bean.ConsultHistoryEntity;

/**
 * Created by Administrator on 2018/1/3.
 */

public interface IConsultHistoryView extends IView {

    void consultHistory(ConsultHistoryEntity entity);

    void getUserId(String userId);
}
