package com.shunlian.app.view;

import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.HotRdEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IAishang extends IView {
   void setNewData(CoreNewEntity coreNewEntity);
   void setHotData(CoreHotEntity coreHotEntity);
   void setHotsData(List<CoreHotEntity.Hot.Goods> mData,String page,String total);
   void setNewsData(List<CoreNewsEntity.Goods> mData,String page,String total);
   void setPushData(List<HotRdEntity.MData> mData,HotRdEntity data);
   void setPingData(CorePingEntity corePingEntity);}
