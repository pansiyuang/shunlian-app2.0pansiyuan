package com.shunlian.app.view;

import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.GetListFilterEntity;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface CategoryFiltrateView extends IView {
    void getListFilter(GetListFilterEntity getListFilterEntity);
    void getGps(DistrictGetlocationEntity districtGetlocationEntity);
}
