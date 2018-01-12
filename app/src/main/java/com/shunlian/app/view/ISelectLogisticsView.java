package com.shunlian.app.view;

import com.shunlian.app.bean.LogisticsNameEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public interface ISelectLogisticsView extends IView {

    /**
     * 选择物流
     * @param logistics
     */
    void selectLogistics(List<LogisticsNameEntity.LogisticsName> logistics);

    /**
     * 设置字母排序
     * @param first_letter_list
     */
    void setLetterSort(List<String> first_letter_list);
}
