package com.shunlian.app.view;

import com.shunlian.app.bean.SortFragEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 */

public interface ISortFragView extends IView {

    /**
     * 左侧大分类
     * @param toplists
     */
    void toplist(List<SortFragEntity.Toplist> toplists);

    /**
     * 右侧子分类
     * @param subLists
     */
    void subRightList(List<SortFragEntity.SubList> subLists,List<SortFragEntity.ItemList> subAllItemLists);
}
