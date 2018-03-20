package com.shunlian.app.view;

import com.shunlian.app.bean.GuanzhuEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public interface IGuanzhuView extends IView {
    /**
     * 关注列表
     * @param listBeans
     * @param page
     * @param allpage
     */
    void  setGuanzhuList(List<GuanzhuEntity.DynamicListBean> listBeans,int page,int allpage);
}
