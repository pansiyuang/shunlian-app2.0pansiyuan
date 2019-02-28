package com.shunlian.app.view;

import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.bean.HotsaleEntity;
import com.shunlian.app.bean.HotsaleHomeEntity;
import com.shunlian.app.bean.KoubeiSecondEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IKoubei extends IView {
   void setHomeData(HotsaleHomeEntity hotsaleHomeEntity);
   void setHotsaleCate(HotsaleEntity hotsaleEntity);
   void setHotsaleCates(KoubeiSecondEntity koubeiSecondEntity);
   void getZan(CommonEntity commonEntity);

}

