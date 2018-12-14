package com.shunlian.app.view;

import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public interface IMemberPageView extends IView {
  void memberListInfo(List<MemberInfoEntity.MemberList> memberLists,int currentPage);

  void memberDetail(MemberInfoEntity memberInfoEntity,String total_num);

  void setWeixin(String weixin);
}
