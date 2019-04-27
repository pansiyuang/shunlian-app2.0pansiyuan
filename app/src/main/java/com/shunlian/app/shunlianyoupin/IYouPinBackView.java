package com.shunlian.app.shunlianyoupin;


import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.view.IView;

import java.util.List;

/**
 * Created by Administrator on 2019/4/3.
 */

public interface IYouPinBackView extends IView {

    void getPingPaiData(YouPingListEntity entity,int babyAllPage,int babyPage,List<YouPingListEntity.lists> lists);
    void getBannerData(List<YouPingbannerEntity.banner> activityListEntity);
}
