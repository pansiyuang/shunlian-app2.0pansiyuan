package com.shunlian.app.view;

import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.GetListFilterEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface CategoryFiltrateView extends IView {
    void getListFilter(GetListFilterEntity getListFilterEntity);
    void getGps(DistrictGetlocationEntity districtGetlocationEntity);
    void initPingpai(List<GetListFilterEntity.Recommend> recommends);
}
