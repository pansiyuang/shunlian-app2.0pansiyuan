package com.shunlian.app.view;

import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.SortFragEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 */

public interface ISortFragView extends IView {

    /**
     * 分类所有类目
     * @param categoryList
     */
    void categoryAll(List<SortFragEntity.Toplist> categoryList);

    /**
     * 设置搜索关键字
     * @param keyworld
     */
    void setKeyworld(GetDataEntity.KeyWord keyworld);
}
