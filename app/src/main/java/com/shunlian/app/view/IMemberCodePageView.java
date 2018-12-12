package com.shunlian.app.view;

import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.MemberInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public interface IMemberCodePageView extends IView {
  /**
   * 邀请码详情
   * @param bean
   */
  default void codeInfo(MemberCodeListEntity bean, String error,boolean intoPage){}

  void bindSuccess(String code);
}
