package com.shunlian.app.view;

import com.shunlian.app.bean.ExpertEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/24.
 */

public interface ISearchExpertView extends IView {

    void getExpertList(List<ExpertEntity.Expert> list, int page, int totalPage);

    void focusUser(int isFocus,String memberId);
}
