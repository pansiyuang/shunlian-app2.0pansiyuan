package com.shunlian.app.view;

import com.shunlian.app.bean.PlusOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public interface IPlusOrderView extends IView {

    void getPlusOrderList(int page, int totalPage, List<PlusOrderEntity.PlusOrder> plusOrderList);
}
