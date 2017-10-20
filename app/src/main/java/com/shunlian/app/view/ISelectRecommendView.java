package com.shunlian.app.view;

import com.shunlian.app.bean.MemberCodeListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface ISelectRecommendView extends IView {

    void selectCodeList(List<MemberCodeListEntity.ListBean> listBeens);

}
