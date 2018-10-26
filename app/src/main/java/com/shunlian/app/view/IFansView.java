package com.shunlian.app.view;

import com.shunlian.app.bean.MemberEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

public interface IFansView extends IView {

    void getFansList(List<MemberEntity.Member> fansList, int page, int totalPage);

    void focusUser(int isFocus, String memberId);

}