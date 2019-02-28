package com.shunlian.app.view;

import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.TeamListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public interface ITeamPageView extends IView {
  void teamListInfo(List<TeamListEntity.TeamUserData> teamUserDataList, TeamListEntity listEntity, int currentPage);
}
