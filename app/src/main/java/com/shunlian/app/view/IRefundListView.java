package com.shunlian.app.view;

import com.shunlian.app.bean.RefundListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public interface IRefundListView extends IView{

    void refundList(List<RefundListEntity.RefundList> refundLists,int page,int allPage);
}
